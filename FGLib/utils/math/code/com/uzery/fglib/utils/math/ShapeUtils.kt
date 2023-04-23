package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.geom.Shape

interface ShapeUtils {
    companion object {
        fun rect(shape: Shape) = RectN(shape.L, shape.S)

        fun into(first: RectN, second: RectN): Boolean {
            return (0 until first.L.dimension()).all { i -> first.L[i]<second.R[i] && second.L[i]<first.R[i] }
        }

        fun into(first: Shape, second: Shape): Boolean {
            //todo when(shape)
            return into(rect(first), rect(second))
        }

        fun interpolate(start: RectN, finish: RectN, k: Double): RectN {
            return RectN.rectLR(start.L + (finish.L - start.L)*k, start.R + (finish.R - start.R)*k)
        }
    }
}
