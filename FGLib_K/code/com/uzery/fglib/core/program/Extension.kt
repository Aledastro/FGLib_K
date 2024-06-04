package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

abstract class Extension(vararg children: Extension) {
    val children = ArrayList<Extension>()

    private val real_children = ArrayList<Extension>()
    private val new_children = ArrayList<Pair<Extension, Boolean>>()

    val stats = ExtensionData()

    fun mouseIn(): Boolean {
        //todo: size -> bounds
        if (stats.size == PointN.ZERO) return false
        return RectN(stats.real_pos, stats.size).into(mouse.pos)
    }

    fun mouseAt(): Boolean {
        fun notAtChildren(e: Extension): Boolean {
            return e.children.all { !it.mouseIn() && notAtChildren(it) }
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
        children.forEach { new_children.add(Pair(it, true)) }
    }

    fun remove(vararg children: Extension) {
        children.forEach { new_children.add(Pair(it, false)) }
    }

    fun clearChildren() = remove(*real_children.toTypedArray())

    init {
        add(*children)
    }

    protected open fun init() {}
    protected open fun initAfter() {}

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
        val toAdd = ArrayList<Extension>()
        val toRemove = ArrayList<Extension>()

        new_children.forEach { pair ->
            val e = pair.first
            val isAdd = pair.second

            toAdd.remove(e)
            toRemove.remove(e)

            if (isAdd) toAdd.add(e)
            else toRemove.add(e)
        }

        new_children.clear()

        real_children.removeAll(toRemove.toSet())
        real_children.removeAll(toAdd.toSet())
        real_children.addAll(toAdd)

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
        fun reset() {
            graphics.setFullDefaults()
            graphics.transform = stats.full_transform*graphics.default_transform
        }
        reset()
        draw(pos)

        real_children.forEach { e ->
            if (e.mode.draw && e.active().draw) {
                e.drawWithChildren(pos+e.stats.render_pos)
            }
        }

        reset()
        drawAfter(pos)

        draw_time++
    }

    internal fun updateTasksWithChildren() {
        mode = next_mode
        onBackGround()

        real_children.forEach {
            it.stats.owner = this
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

    fun switchTo(show: Boolean) {
        if (show) show()
        else hide()
    }

    fun switchTo(mode: MODE) {
        when (mode) {
            MODE.SHOW -> show()
            MODE.HIDE -> hide()
            MODE.ONLY_DRAW -> only_draw()
            MODE.ONLY_UPDATE -> only_update()
        }
    }

    fun switch() = switchTo(-mode)
}
