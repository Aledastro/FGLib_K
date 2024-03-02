package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

abstract class Extension(vararg children: Extension) {
    val children = ArrayList<Extension>()

    private val real_children = ArrayList<Extension>()
    private val new_children = ArrayList<Extension>()
    private val old_children = ArrayList<Extension>()

    val data = ExtensionData()

    var owner: Extension? = null

    fun mouseIn(): Boolean {
        //todo: size -> bounds
        if (data.size == PointN.ZERO) return false
        return RectN(data.render_pos+data.pos, data.size).into(mouse.pos)
    }

    fun mouseAt(): Boolean {
        fun notAtChildren(e: Extension): Boolean {
            return e.children.all { !it.mouseIn() && notAtChildren(it)  }
        }
        return mouseIn() && notAtChildren(this)
    }

    protected var full_time = 0
        private set
    protected var update_time = 0
        private set
    protected var draw_time = 0
        private set
    protected var show_time = 0
        private set
    protected var hide_time = 0
        private set
    protected var only_draw_time = 0
        private set
    protected var only_update_time = 0
        private set

    enum class MODE(val draw: Boolean, val update: Boolean) {
        SHOW(true, true),
        HIDE(false, false),
        ONLY_DRAW(true, false),
        ONLY_UPDATE(false, true);

        fun active() = draw || update

        operator fun unaryMinus(): MODE {
            return when (this) {
                SHOW -> HIDE
                HIDE -> SHOW
                ONLY_DRAW -> ONLY_UPDATE
                ONLY_UPDATE -> ONLY_DRAW
            }
        }
    }

    var mode = MODE.SHOW
        private set
    private var next_mode = MODE.SHOW

    open fun active() = MODE.SHOW

    fun add(vararg children: Extension) {
        new_children.addAll(children)
    }

    fun remove(vararg children: Extension) {
        old_children.addAll(children)
    }

    fun clearChildren() = remove(*real_children.toTypedArray())

    init {
        add(*children)
    }

    open fun init() {}
    open fun initAfter() {}

    open fun update() {}
    open fun updateAfter() {}

    open fun draw(pos: PointN) {}
    open fun drawAfter(pos: PointN) {}

    open fun onShow() {}
    open fun onHide() {}

    open fun onOnlyDraw() {}
    open fun onOnlyUpdate() {}

    internal fun initWithChildren() {
        modify()
        init()
        modify()
        real_children.forEach { it.initWithChildren() }
        initAfter()
    }

    private fun modify() {
        real_children.addAll(new_children)
        new_children.clear()
        real_children.removeAll(old_children.toSet())
        old_children.clear()

        children.clear()
        children.addAll(real_children)
    }

    internal fun updateWithChildren() {
        modify()

        update()
        real_children.forEach { if (it.mode.update && it.active().update) it.updateWithChildren() }
        updateAfter()

        update_time++
    }

    internal fun drawWithChildren(pos: PointN) {
        graphics.setDefaults()
        graphics.drawPOS = PointN.ZERO
        draw(pos)

        real_children.forEach { e ->
            if (e.mode.draw && e.active().draw) {
                e.drawWithChildren(pos+e.data.pos+e.data.draw_pos)
            }
        }

        graphics.setDefaults()
        graphics.drawPOS = PointN.ZERO
        drawAfter(pos)

        draw_time++
    }

    internal fun updateTasksWithChildren() {
        mode = next_mode
        onBackGround()

        real_children.forEach {
            it.owner = this
            it.data.render_pos = data.render_pos+data.pos
            it.updateTasksWithChildren()
        }

        children.clear()
        children.addAll(real_children)

        full_time++
        updateModeTime()
    }

    open fun onBackGround() {}

    private fun resetModeTime() {
        show_time = 0
        hide_time = 0
        only_draw_time = 0
        only_update_time = 0
    }

    private fun updateModeTime() {
        when (mode) {
            MODE.SHOW -> show_time++
            MODE.HIDE -> hide_time++
            MODE.ONLY_DRAW -> only_draw_time++
            MODE.ONLY_UPDATE -> only_update_time++
        }
    }

    fun show() {
        next_mode = MODE.SHOW
        onShow()
        resetModeTime()
    }

    fun hide() {
        next_mode = MODE.HIDE
        onHide()
        resetModeTime()
    }

    fun only_draw() {
        next_mode = MODE.ONLY_DRAW
        onOnlyDraw()
        resetModeTime()
    }

    fun only_update() {
        next_mode = MODE.ONLY_UPDATE
        onOnlyDraw()
        resetModeTime()
    }

    fun switch() {
        when (mode) {
            MODE.SHOW -> hide()
            MODE.HIDE -> show()
            MODE.ONLY_DRAW -> only_update()
            MODE.ONLY_UPDATE -> only_draw()
        }
    }
}
