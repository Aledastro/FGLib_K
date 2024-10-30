package com.uzery.fglib.core.component.reaction

import com.uzery.fglib.core.component.ObjectComponent

fun interface OnDeathComponent: ObjectComponent {
    fun run()
}
