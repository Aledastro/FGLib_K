package com.uzery.fglib.utils.struct.num

data class DoubleD(val x: Double, val y: Double) {
    constructor(): this(0.0, 0.0)

    val width = x
    val height = y

    val w = width
    val h = height

    operator fun times(other: DoubleD) = DoubleD(x*other.x, y*other.y)

    operator fun div(other: DoubleD) = DoubleD(x/other.x, y/other.y)

    operator fun plus(other: DoubleD) = DoubleD(x+other.x, y+other.y)

    operator fun minus(other: DoubleD) = DoubleD(x-other.x, y-other.y)

    operator fun times(other: Int) = DoubleD(x*other, y*other)
    operator fun div(other: Int) = DoubleD(x/other, y/other)

}
