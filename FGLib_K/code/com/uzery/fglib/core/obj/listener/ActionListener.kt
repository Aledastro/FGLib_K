package com.uzery.fglib.core.obj.listener

import com.uzery.fglib.core.obj.ObjectComponent

fun interface ActionListener: ObjectComponent {
    fun activate(action: InputAction)
}
