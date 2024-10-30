package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.listener.InputAction

interface TempAction {
    fun next()

    fun activate(action: InputAction) {
        /* ignore */
    }

    val ends: Boolean
}
