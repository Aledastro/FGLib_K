package com.uzery.fglib.core.obj

class GroupComponent(vararg component: ObjectComponent): ObjectComponent{
    val components = ArrayList<ObjectComponent>()
    init {
        components.addAll(component)
    }

    fun add(component: ObjectComponent){
        components.add(component)
    }
}
