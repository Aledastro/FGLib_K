package com.uzery.fglib.core.component

open class GroupComponent(vararg component: ObjectComponent): ObjectComponent, HavingComponentSyntax {
    val components = ArrayList<ObjectComponent>()

    init {
        components.addAll(component)
    }

    override fun addComponent(vararg component: ObjectComponent) {
        components.addAll(component)
    }
}
