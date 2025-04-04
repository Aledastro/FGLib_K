package com.uzery.fglib.utils.input.combination

import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

/**
 * TODO("doc")
 **/
class FGKeyCombination(construct: FGKeyCombination.() -> Unit) {
    val main = FGKeyCombinationPart {}
    val parts = ArrayList<FGKeyCombinationPart>()

    fun combination(part: FGKeyCombinationPart) {
        parts.add(part)
    }

    fun combination(construct: FGKeyCombinationPart.() -> Unit) {
        parts.add(FGKeyCombinationPart { construct() })
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGKey) {
        main.pressed(*key)
    }

    fun notPressed(vararg key: FGKey) {
        main.notPressed(*key)
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

    fun notPressed(vararg key: FGMouseKey) {
        main.notPressed(*key)
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
