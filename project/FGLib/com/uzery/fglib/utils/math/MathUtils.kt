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

        fun getDegree(cx: Double, cy: Double, px: Double, py: Double): Double {
            var alpha = PI/2
            if(cx != px) alpha = atan((py - cy)/(px - cx))
            if(cx>px) alpha += PI
            if(cx == px && cy>py) alpha += PI
            return alpha
        }

        fun getDegree(px: Double, py: Double) = getDegree(0.0, 0.0, px, py)
        fun getDegree(c: PointN, p: PointN) = getDegree(c.X, c.Y, p.X, p.Y)
        fun getDegree(p: PointN) = getDegree(0.0, 0.0, p.X, p.Y)
    }
}
