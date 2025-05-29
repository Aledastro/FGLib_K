package com.uzery.fglib.core.component.reaction

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.obj.GameObject

/**
 * One of basic [ObjectComponent]
 *
 * Triggered once at [GameObject] death
 **/
fun interface OnDeathComponent: ObjectComponent {
    fun run()
}
