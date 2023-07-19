package com.uzery.fglib.utils.math.geom

import kotlin.math.max

abstract class Shape {
    abstract fun copy(move: PointN): Shape
    abstract fun into(pos: PointN): Boolean
    fun dimension() = max(L.dimension(), R.dimension())

    abstract val L: PointN
    abstract val R: PointN
    val C: PointN
        get() = (L+R)/2

    val S: PointN
        get() = R-L

    abstract val code: Code

    enum class Code {
        RECT, OVAL
    }
}