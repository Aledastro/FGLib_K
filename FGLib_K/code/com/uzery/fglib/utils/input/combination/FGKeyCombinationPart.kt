package com.uzery.fglib.utils.input.combination

import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey

/**
 * TODO("doc")
 **/
class FGKeyCombinationPart(construct: FGKeyCombinationPart.() -> Unit) {
    private val pressed = ArrayList<FGKey>()
    private val notPressed = ArrayList<FGKey>()
    private val inPressed = ArrayList<FGKey>()
    private val rePressed = ArrayList<FGKey>()
    private val periodPressed = ArrayList<Triple<Int, Int, FGKey>>()

    private val mousePressed = ArrayList<FGMouseKey>()
    private val mouseNotPressed = ArrayList<FGMouseKey>()
    private val mouseInPressed = ArrayList<FGMouseKey>()
    private val mouseRePressed = ArrayList<FGMouseKey>()
    private val mousePeriodPressed = ArrayList<Triple<Int, Int, FGMouseKey>>()

    init {
        construct(this)
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGKey) {
        pressed.addAll(key)
    }

    fun notPressed(vararg key: FGKey) {
        notPressed.addAll(key)
    }

    fun inPressed(vararg key: FGKey) {
        inPressed.addAll(key)
    }

    fun rePressed(vararg key: FGKey) {
        rePressed.addAll(key)
    }

    fun periodPressed(period: Int, key: FGKey) {
        periodPressed.add(Triple(period, period, key))
    }

    fun periodPressed(start_time: Int, period: Int, key: FGKey) {
        periodPressed.add(Triple(start_time, period, key))
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun pressed(vararg key: FGMouseKey) {
        mousePressed.addAll(key)
    }

    fun notPressed(vararg key: FGMouseKey) {
        mouseNotPressed.addAll(key)
    }

    fun inPressed(vararg key: FGMouseKey) {
        mouseInPressed.addAll(key)
    }

    fun rePressed(vararg key: FGMouseKey) {
        mouseRePressed.addAll(key)
    }

    fun periodPressed(period: Int, key: FGMouseKey) {
        mousePeriodPressed.add(Triple(period, period, key))
    }

    fun periodPressed(start_time: Int, period: Int, key: FGMouseKey) {
        mousePeriodPressed.add(Triple(start_time, period, key))
    }

    //////////////////////////////////////////////////////////////////////////////////////

    fun active(): Boolean {
        val k1 = pressed.all { keyboard.pressed(it) }
        val k2 = notPressed.all { !keyboard.pressed(it) }
        val k3 = inPressed.all { keyboard.inPressed(it) }
        val k4 = rePressed.all { keyboard.rePressed(it) }
        val k5 = periodPressed.all { keyboard.periodPressed(it.first, it.second, it.third) }
        val b1 = k1 && k2 && k3 && k4 && k5

        val m1 = mousePressed.all { mouse.keys.pressed(it) }
        val m2 = mouseNotPressed.all { !mouse.keys.pressed(it) }
        val m3 = mouseInPressed.all { mouse.keys.inPressed(it) }
        val m4 = mouseRePressed.all { mouse.keys.rePressed(it) }
        val m5 = mousePeriodPressed.all { mouse.keys.periodPressed(it.first, it.second, it.third) }
        val b2 = m1 && m2 && m3 && m4 && m5

        return b1 && b2
    }

    fun isEmpty(): Boolean {
        return pressed.isEmpty() && inPressed.isEmpty() && rePressed.isEmpty() && periodPressed.isEmpty()
    }
}
