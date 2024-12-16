package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.listener.InputAction

/**
 * Basic temporary action for [Controller]
 *
 * Updates every frame, also reacts on [InputAction] activation
 *
 * After action ends [Controller] chooses another [InputAction]
 **/
interface TempAction {
    fun next()

    fun activate(action: InputAction) {
        /* ignore */
    }

    val ends: Boolean
}
