package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.PI
import kotlin.math.atan

interface MathUtils {
    companion object {
        fun mod(input: Double, mod: Double) = input%mod + (if(input<0) mod else 0.0)

        fun mod(input: Int, mod: Int) = input%mod + (if(input<0) mod else 0)


        fun min(vararg xs: Double) = xs.min()

        fun max(vararg xs: Double) = xs.max()

        fun getDegree(p: PointN): Double {
            var alpha = PI/2
            if(p.X != 0.0) alpha = atan(p.Y/p.X)
            if(p.X<0.0 || p.X == 0.0 && p.Y<0.0) alpha += PI
            return alpha
        }

        fun getDegree(c: PointN, p: PointN) = getDegree(p - c)
    }
}
