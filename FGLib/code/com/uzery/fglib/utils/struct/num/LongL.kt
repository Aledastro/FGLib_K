package com.uzery.fglib.utils.struct.num

/**
 * TODO("doc")
 **/
data class LongL(val x: Long, val y: Long) {
    constructor(): this(0L, 0L)

    val width = x
    val height = y

    val w = width
    val h = height

    val indices
        get() = Array((width*height).toInt()) { i -> LongL(i%width, i/width) }
    val xs
        get() = Array(width.toInt()) { i -> i }
    val ys
        get() = Array(height.toInt()) { j -> j }

    operator fun times(other: LongL) = LongL(x*other.x, y*other.y)

    operator fun div(other: LongL) = LongL(x/other.x, y/other.y)

    operator fun plus(other: LongL) = LongL(x+other.x, y+other.y)

    operator fun minus(other: LongL) = LongL(x-other.x, y-other.y)

    operator fun times(other: Int) = LongL(x*other, y*other)
    operator fun div(other: Int) = LongL(x/other, y/other)

}
