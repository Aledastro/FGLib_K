package com.uzery.fglib.utils.struct.num

/**
 * TODO("doc")
 **/
data class IntI(val x: Int, val y: Int) {
    constructor(): this(0, 0)

    val width = x
    val height = y

    val w = width
    val h = height

    val indices
        get() = Array(width*height) { i -> IntI(i%width, i/width) }
    val xs
        get() = Array(width) { i -> i }
    val ys
        get() = Array(height) { j -> j }

    operator fun times(other: IntI) = IntI(x*other.x, y*other.y)

    operator fun div(other: IntI) = IntI(x/other.x, y/other.y)

    operator fun plus(other: IntI) = IntI(x+other.x, y+other.y)

    operator fun minus(other: IntI) = IntI(x-other.x, y-other.y)

    operator fun times(other: Int) = IntI(x*other, y*other)
    operator fun div(other: Int) = IntI(x/other, y/other)

}
