package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.geom.Shape

object ShapeUtils {
    fun rect(shape: Shape) = RectN(shape.L, shape.S)

    fun into(first: RectN, second: RectN): Boolean {
        return (0 until first.dimension()).all { i -> first.L[i] < second.R[i] && second.L[i] < first.R[i] }
    }

    fun into(first: Shape, second: Shape): Boolean {
        //todo when(shape)
        return into(rect(first), rect(second))
    }

    fun interpolate(start: RectN, finish: RectN, k: Double): RectN {
        return RectN.rectLR(start.L.interpolate(finish.L, k), start.R.interpolate(finish.R, k))
    }

    fun rectX(first: RectN, second: RectN): RectN {
        fun minP(a: PointN, b: PointN) = PointN.transform(a, b) { x, y -> kotlin.math.min(x, y) }
        fun maxP(a: PointN, b: PointN) = PointN.transform(a, b) { x, y -> kotlin.math.max(x, y) }

        return RectN.rectLR(minP(first.L, second.L), maxP(first.R, second.R))
    }

    fun rectX(first: Shape, second: Shape): RectN {
        return rectX(rect(first), rect(second))
    }
}
