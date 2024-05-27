package com.uzery.fglib.utils.math.scale

import com.uzery.fglib.utils.MathUtils

data class GradientScale(val f: (Double) -> Double) {
    fun linear(input: Double): Double = f(input.coerceIn(0.0, 1.0))
    fun cycled(input: Double): Double = f(MathUtils.mod(input, 1.0))

    fun swing(input: Double): Double {
        val x = MathUtils.mod(input, 2.0)
        return if (x < 1) f(x) else f(2-x)
    }
}
