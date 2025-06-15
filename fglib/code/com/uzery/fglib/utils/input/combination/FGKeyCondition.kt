package com.uzery.fglib.utils.input.combination

import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

sealed class FGKeyCondition {
    abstract fun active(): Boolean

    data class PRESSED(val key: FGKey): FGKeyCondition() {
        override fun active() = keyboard.pressed(key)
    }

    data class NOT_PRESSED(val key: FGKey): FGKeyCondition() {
        override fun active() = !keyboard.pressed(key)
    }

    data class IN_PRESSED(val key: FGKey): FGKeyCondition() {
        override fun active() = keyboard.inPressed(key)
    }

    data class RE_PRESSED(val key: FGKey): FGKeyCondition() {
        override fun active() = keyboard.rePressed(key)
    }

    data class PERIOD_PRESSED(val start: Int, val period: Int, val key: FGKey): FGKeyCondition() {
        override fun active() = keyboard.periodPressed(start, period, key)
    }

    data class MOUSE_PRESSED(val key: FGMouseKey): FGKeyCondition() {
        override fun active() = mouse.keys.pressed(key)
    }

    data class MOUSE_NOT_PRESSED(val key: FGMouseKey): FGKeyCondition() {
        override fun active() = !mouse.keys.pressed(key)
    }

    data class MOUSE_IN_PRESSED(val key: FGMouseKey): FGKeyCondition() {
        override fun active() = mouse.keys.inPressed(key)
    }

    data class MOUSE_RE_PRESSED(val key: FGMouseKey): FGKeyCondition() {
        override fun active() = mouse.keys.rePressed(key)
    }

    data class MOUSE_PERIOD_PRESSED(val start: Int, val period: Int, val key: FGMouseKey): FGKeyCondition() {
        override fun active() = mouse.keys.periodPressed(start, period, key)
    }
}
