package com.uzery.fglib.core.component.reaction

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.obj.GameObject

/**
 * [OnLoadComponent] is one of basic [ObjectComponent]
 *
 * Triggered once at [GameObject] initialisation
 *
 * Time-consuming init operations recommended to be done here
 **/
fun interface OnLoadComponent: ObjectComponent {
    fun run()
}
