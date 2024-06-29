package com.uzery.fglib.core.obj

open class GroupComponent(vararg component: ObjectComponent): ObjectComponent {
    val components = ArrayList<ObjectComponent>()

    init {
        components.addAll(component)
    }

    fun add(vararg component: ObjectComponent) {
        components.addAll(component)
    }
}
