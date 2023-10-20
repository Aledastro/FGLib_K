package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.math.geom.PointN
import java.util.*

abstract class Extension(vararg children: Extension) {
    val children = LinkedList<Extension>()
    var draw_pos = PointN.ZERO

    var active = true
        private set
    private var next_active = true

    protected fun add(vararg children: Extension) {
        this.children.addAll(children)
    }

    init {
        add(*children)
    }

    open fun init() {}
    open fun initAfter() {}

    open fun next() {}
    open fun nextAfter() {}

    open fun draw(pos: PointN) {}
    open fun drawAfter(pos: PointN) {}

    open fun onShow() {}
    open fun onHide() {}

    internal fun initWithChildren() {
        init()
        children.forEach { it.initWithChildren() }
        initAfter()
    }

    internal fun nextWithChildren() {
        next()
        children.forEach { if (it.active()) it.nextWithChildren() }
        nextAfter()
    }

    internal fun drawWithChildren(pos: PointN) {
        graphics.setDefaults()
        draw(pos)

        children.forEach { if (it.active()) it.drawWithChildren(pos+it.draw_pos) }

        graphics.setDefaults()
        drawAfter(pos)
    }

    internal fun updateTasksWithChildren() {
        active = next_active
        onBackGround()
        children.forEach { it.updateTasksWithChildren() }
    }

    open fun active() = active

    open fun onBackGround() {}

    fun show() {
        next_active = true
        onShow()
    }

    fun hide() {
        next_active = false
        onHide()
    }

    fun switch() {
        next_active = !active
    }
}
