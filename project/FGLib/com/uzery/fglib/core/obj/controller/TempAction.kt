package com.uzery.fglib.core.obj.controller

import com.uzery.fglib.core.obj.ability.InputAction

interface TempAction {
    fun next()

    fun activate(action: InputAction) {
        /* ignore */
    }

    val ends: Boolean
}
