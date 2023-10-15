package com.uzery.fglib.core.program

import java.util.*

abstract class Extension(vararg children: Extension) {
    val children = LinkedList<Extension>()

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
        children.forEach { if (it.running()) it.updateWithChildren() }
        updateAfter()
    }

    open fun running() = true

}
