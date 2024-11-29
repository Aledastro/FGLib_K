package com.uzery.fglib.utils.input.combination

import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

class KeyCombination(construct: KeyCombination.() -> Unit) {
    val main = KeyCombinationPart {}
    val parts = ArrayList<KeyCombinationPart>()

    fun combination(part: KeyCombinationPart) {
        parts.add(part)
    }

    fun combination(construct: KeyCombinationPart.() -> Unit) {
        parts.add(KeyCombinationPart { construct() })
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGKey) {
        main.pressed(*key)
    }

    fun inPressed(vararg key: FGKey) {
        main.inPressed(*key)
    }

    fun rePressed(vararg key: FGKey) {
        main.rePressed(*key)
    }

    fun periodPressed(period: Int, key: FGKey) {
        main.periodPressed(period, key)
    }

    fun periodPressed(start_time: Int, period: Int, key: FGKey) {
        main.periodPressed(start_time, period, key)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGMouseKey) {
        main.pressed(*key)
    }

    fun inPressed(vararg key: FGMouseKey) {
        main.inPressed(*key)
    }

    fun rePressed(vararg key: FGMouseKey) {
        main.rePressed(*key)
    }

    fun periodPressed(period: Int, key: FGMouseKey) {
        main.periodPressed(period, key)
    }

    fun periodPressed(start_time: Int, period: Int, key: FGMouseKey) {
        main.periodPressed(start_time, period, key)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    init {
        construct(this)
    }

    fun active(): Boolean {
        return parts.any { it.active() } || !main.isEmpty() && main.active()
    }
}
