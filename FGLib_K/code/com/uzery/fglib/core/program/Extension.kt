package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.math.geom.PointN

abstract class Extension(vararg children: Extension) {
    private val children = ArrayList<Extension>()
    var draw_pos = PointN.ZERO

    private val new_children = ArrayList<Extension>()
    private val old_children = ArrayList<Extension>()

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
    fun clearChildren() = remove(*children.toTypedArray())

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
        init()
        modify()
        children.forEach { it.initWithChildren() }
        initAfter()
    }

    private fun modify() {
        children.addAll(new_children)
        children.removeAll(old_children.toSet())
    }

    internal fun updateWithChildren() {
        modify()

        update()
        children.forEach { if (it.mode.update && it.active().update) it.updateWithChildren() }
        updateAfter()
    }

    internal fun drawWithChildren(pos: PointN) {
        graphics.setDefaults()
        graphics.drawPOS = PointN.ZERO
        draw(pos)

        children.forEach { if (it.mode.draw && it.active().draw) it.drawWithChildren(pos+it.draw_pos) }

        graphics.setDefaults()
        graphics.drawPOS = PointN.ZERO
        drawAfter(pos)
    }

    internal fun updateTasksWithChildren() {
        mode = next_mode
        onBackGround()
        children.forEach { it.updateTasksWithChildren() }
    }

    open fun onBackGround() {}

    fun show() {
        next_mode = MODE.SHOW
        onShow()
    }

    fun hide() {
        next_mode = MODE.HIDE
        onHide()
    }

    fun only_draw() {
        next_mode = MODE.ONLY_DRAW
        onOnlyDraw()
    }

    fun only_update() {
        next_mode = MODE.ONLY_UPDATE
        onOnlyDraw()
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
