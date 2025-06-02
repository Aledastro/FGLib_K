package com.uzery.fglib.utils.input.combination

import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

/**
 * TODO("doc")
 **/
class FGKeyCombinationPart {
    constructor()

    constructor(construct: FGKeyCombinationPart.() -> Unit) {
        construct(this)
    }

    val actions = ArrayList<FGKeyCondition>()

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGKey) {
        key.forEach { k -> actions.add(FGKeyCondition.PRESSED(k)) }
    }

    fun notPressed(vararg key: FGKey) {
        key.forEach { k -> actions.add(FGKeyCondition.NOT_PRESSED(k)) }
    }

    fun inPressed(vararg key: FGKey) {
        key.forEach { k -> actions.add(FGKeyCondition.IN_PRESSED(k)) }
    }

    fun rePressed(vararg key: FGKey) {
        key.forEach { k -> actions.add(FGKeyCondition.RE_PRESSED(k)) }
    }

    fun periodPressed(period: Int, key: FGKey) {
        actions.add(FGKeyCondition.PERIOD_PRESSED(0, period, key))
    }

    fun periodPressed(start: Int, period: Int, key: FGKey) {
        actions.add(FGKeyCondition.PERIOD_PRESSED(start, period, key))
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGMouseKey) {
        key.forEach { k -> actions.add(FGKeyCondition.MOUSE_PRESSED(k)) }
    }

    fun notPressed(vararg key: FGMouseKey) {
        key.forEach { k -> actions.add(FGKeyCondition.MOUSE_NOT_PRESSED(k)) }
    }

    fun inPressed(vararg key: FGMouseKey) {
        key.forEach { k -> actions.add(FGKeyCondition.MOUSE_IN_PRESSED(k)) }
    }

    fun rePressed(vararg key: FGMouseKey) {
        key.forEach { k -> actions.add(FGKeyCondition.MOUSE_RE_PRESSED(k)) }
    }

    fun periodPressed(period: Int, key: FGMouseKey) {
        actions.add(FGKeyCondition.MOUSE_PERIOD_PRESSED(0, period, key))
    }

    fun periodPressed(start: Int, period: Int, key: FGMouseKey) {
        actions.add(FGKeyCondition.MOUSE_PERIOD_PRESSED(start, period, key))
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun active(): Boolean {
        return actions.all { it.active() }
    }

    fun isEmpty(): Boolean {
        return actions.isEmpty()
    }
}
