package com.uzery.fglib.core.component.reaction

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.obj.GameObject

/**
 * [OnGrabComponent] is one of basic [ObjectComponent]
 *
 * Triggered once when [GameObject] is grabbed
 **/
fun interface OnGrabComponent: ObjectComponent {
    fun run()
}
