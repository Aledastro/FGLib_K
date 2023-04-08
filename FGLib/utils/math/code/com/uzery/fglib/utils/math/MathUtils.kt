package com.uzery.fglib.utils.math

interface MathUtils {
    companion object {
        fun concat(input: Double, start: Double, end: Double): Double = input.coerceAtMost(start).coerceAtLeast(end)

        fun concat(input: Double, start: Int, end: Int): Double = concat(input, start.toDouble(), end.toDouble())

        fun concat(input: Int, start: Int, end: Int): Int = input.coerceAtMost(start).coerceAtLeast(end)
        fun mod(input: Double, mod: Double): Double {
            return input%mod + (if(input<0) mod else 0.0)
        }
    }
}
