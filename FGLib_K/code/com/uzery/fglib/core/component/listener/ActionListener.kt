package com.uzery.fglib.core.component.listener

import com.uzery.fglib.core.component.ObjectComponent

/**
 * [ActionListener] is one of basic [ObjectComponent]
 *
 * Reacts on [InputAction] activation
 **/
fun interface ActionListener: ObjectComponent {
    fun activate(action: InputAction)
}
