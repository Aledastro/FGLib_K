package com.uzery.fglib.core.obj.component

import com.uzery.fglib.core.obj.ObjectComponent

fun interface OnDeathComponent: ObjectComponent {
    fun run()
}
