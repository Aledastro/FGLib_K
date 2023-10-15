package com.uzery.fglib.core.program

import java.util.*

abstract class Extension(vararg children: Extension) {
    val children = LinkedList<Extension>()
    var visible = true
        private set
    private var next_visible = true

    init {
        this.children.addAll(children)
    }

    abstract fun init()
    abstract fun update()

    open fun updateAfter() {

    }

    internal fun initWithChildren() {
        init()
        children.forEach { it.initWithChildren() }
    }

    internal fun updateWithChildren() {
        update()
        children.forEach { if (it.visible()) it.updateWithChildren() }
        updateAfter()
    }
    internal fun updateVisibilityWithChildren() {
        visible = next_visible
        children.forEach { it.updateVisibilityWithChildren() }
    }

    open fun visible() = visible

    fun show(){
        next_visible = true
    }
    fun hide(){
        next_visible = false
    }

    fun switch(){
        next_visible = !visible
    }
}
