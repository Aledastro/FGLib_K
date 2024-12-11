package com.uzery.fglib.utils.struct.num

/**
 * TODO("doc")
 **/
data class FloatF(val x: Float, val y: Float) {
    constructor(): this(0.0F, 0.0F)

    val width = x
    val height = y

    val w = width
    val h = height

    operator fun times(other: FloatF) = FloatF(x*other.x, y*other.y)

    operator fun div(other: FloatF) = FloatF(x/other.x, y/other.y)

    operator fun plus(other: FloatF) = FloatF(x+other.x, y+other.y)

    operator fun minus(other: FloatF) = FloatF(x-other.x, y-other.y)

    operator fun times(other: Int) = FloatF(x*other, y*other)
    operator fun div(other: Int) = FloatF(x/other, y/other)

}
