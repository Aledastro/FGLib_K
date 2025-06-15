package com.uzery.fglib.utils.input.combination

import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

/**
 * TODO("doc")
 **/
class FGKeyCombination {
    constructor(vararg part: FGKeyCombinationPart) {
        parts.addAll(part)
    }

    constructor(construct: FGKeyCombination.() -> Unit) {
        construct(this)
    }

    private val inner = FGKeyCombinationPart()
    private val parts = ArrayList<FGKeyCombinationPart>()

    fun getCombinations(): List<FGKeyCombinationPart> {
        return ArrayList<FGKeyCombinationPart>().apply {
            if (!inner.isEmpty()) add(inner)
            parts.forEach { part -> if (!part.isEmpty()) add(part) }
        }
    }

    fun combination(part: FGKeyCombinationPart) {
        parts.add(part)
    }

    fun combination(construct: FGKeyCombinationPart.() -> Unit) {
        parts.add(FGKeyCombinationPart { construct() })
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGKey) {
        inner.pressed(*key)
    }

    fun notPressed(vararg key: FGKey) {
        inner.notPressed(*key)
    }

    fun inPressed(vararg key: FGKey) {
        inner.inPressed(*key)
    }

    fun rePressed(vararg key: FGKey) {
        inner.rePressed(*key)
    }

    fun periodPressed(period: Int, key: FGKey) {
        inner.periodPressed(period, key)
    }

    fun periodPressed(start_time: Int, period: Int, key: FGKey) {
        inner.periodPressed(start_time, period, key)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGMouseKey) {
        inner.pressed(*key)
    }

    fun notPressed(vararg key: FGMouseKey) {
        inner.notPressed(*key)
    }

    fun inPressed(vararg key: FGMouseKey) {
        inner.inPressed(*key)
    }

    fun rePressed(vararg key: FGMouseKey) {
        inner.rePressed(*key)
    }

    fun periodPressed(period: Int, key: FGMouseKey) {
        inner.periodPressed(period, key)
    }

    fun periodPressed(start_time: Int, period: Int, key: FGMouseKey) {
        inner.periodPressed(start_time, period, key)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun active(): Boolean {
        return parts.any { it.active() } || !inner.isEmpty() && inner.active()
    }
}
