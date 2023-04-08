package com.uzery.fglib.utils.math.geom

abstract class Shape {
    abstract val L: PointN
    abstract val R: PointN
    val C: PointN
        get() = (L+R)/2

    val S: PointN
        get() = R-L
}