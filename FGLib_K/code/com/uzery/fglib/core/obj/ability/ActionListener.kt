package com.uzery.fglib.core.obj.ability

import com.uzery.fglib.core.obj.ObjectComponent

fun interface ActionListener: ObjectComponent {
    fun activate(action: InputAction)
}
