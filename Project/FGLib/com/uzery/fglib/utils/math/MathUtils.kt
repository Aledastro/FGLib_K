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

        fun getDegree(px: Double, py: Double, mx: Double, my: Double): Double {
            var alpha = PI / 2
            if (px != mx) alpha = atan((my - py) / (mx - px))
            if (px > mx) alpha += PI
            if (px == mx && py > my) alpha += PI
            return alpha
        }
        fun getDegree(p: PointN,m: PointN): Double {
            return getDegree(p.X(),p.Y(),m.X(),m.Y())
        }
    }
}
