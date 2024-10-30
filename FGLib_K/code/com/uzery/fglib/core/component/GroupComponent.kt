package com.uzery.fglib.core.component

open class GroupComponent(vararg component: ObjectComponent): ObjectComponent, ComponentSyntaxable {
    val components = ArrayList<ObjectComponent>()

    init {
        components.addAll(component)
    }

    override fun add(vararg component: ObjectComponent) {
        components.addAll(component)
    }
}
