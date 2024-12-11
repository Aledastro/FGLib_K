package com.uzery.fglib.core.component.ability

import com.uzery.fglib.core.component.ObjectComponent

/**
 * [AbilityBox] is one of basic [ObjectComponent]
 *
 * It called function run() every frame
 **/
fun interface AbilityBox: ObjectComponent {
    fun run()
}
