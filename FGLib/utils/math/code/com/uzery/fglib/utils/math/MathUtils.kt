package com.uzery.fglib.utils.math

interface MathUtils {
    companion object {
        fun mod(input: Double, mod: Double) = input%mod + (if(input<0) mod else 0.0)

        fun mod(input: Int, mod: Int) = input%mod + (if(input<0) mod else 0)


        fun min(vararg xs: Double) = xs.min()

        fun max(vararg xs: Double) = xs.max()
    }
}
