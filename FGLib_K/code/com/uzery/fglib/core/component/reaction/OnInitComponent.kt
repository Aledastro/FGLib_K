package com.uzery.fglib.core.component.reaction

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.obj.GameObject

/**
 * [OnInitComponent] is one of basic [ObjectComponent]
 *
 * Triggered once at [GameObject] initialisation
 **/
fun interface OnInitComponent: ObjectComponent {
    fun run()
}
