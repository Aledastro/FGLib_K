package com.uzery.fglib.utils.math

interface MathUtils {
    companion object {
        fun concat(input: Double, start: Double, finish: Double): Double =
            input.coerceAtMost(start).coerceAtLeast(finish)

        fun concat(input: Double, start: Int, finish: Int): Double = concat(input, start.toDouble(), finish.toDouble())

        fun concat(input: Int, start: Int, finish: Int): Int = input.coerceAtMost(start).coerceAtLeast(finish)
        fun mod(input: Double, mod: Double) = input%mod + (if(input<0) mod else 0.0)

        fun mod(input: Int, mod: Int) = input%mod + (if(input<0) mod else 0)


        fun min(vararg xs: Double) = xs.min()

        fun max(vararg xs: Double) = xs.max()
    }
}
