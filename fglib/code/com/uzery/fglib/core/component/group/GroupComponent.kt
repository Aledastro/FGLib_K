package com.uzery.fglib.core.component.group

import com.uzery.fglib.core.component.ComponentFunctionality
import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.obj.GameObject

/**
 * One of basic [ObjectComponent]
 *
 * Adding this to [GameObject] is equivalent of adding all it's children
 *
 * Can add elements via [ComponentFunctionality]
 **/
open class GroupComponent(vararg component: ObjectComponent): ComponentFunctionality(), ObjectComponent {
    val components = ArrayList<ObjectComponent>()

    init {
        components.addAll(component)
    }

    final override fun addComponent(vararg component: ObjectComponent) {
        components.addAll(component)
    }
}
