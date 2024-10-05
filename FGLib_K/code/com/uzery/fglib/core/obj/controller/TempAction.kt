package com.uzery.fglib.core.obj.controller

import com.uzery.fglib.core.obj.listener.InputAction

interface TempAction {
    fun next()

    fun activate(action: InputAction) {
        /* ignore */
    }

    val ends: Boolean
}
