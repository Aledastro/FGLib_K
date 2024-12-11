package com.uzery.fglib.core.component.listener

import com.uzery.fglib.core.component.ObjectComponent

/**
 * TODO("doc")
 **/
fun interface ActionListener: ObjectComponent {
    fun activate(action: InputAction)
}
