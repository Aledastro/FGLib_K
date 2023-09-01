package com.uzery.fglib.utils.math.num

data class IntI(val width: Int, val height: Int) {
    constructor(): this(0, 0)

    val indices
        get() = Array(width*height) { i -> IntI(i%width, i/width) }

    operator fun times(other: IntI) = IntI(width*other.width, height*other.height)

    operator fun div(other: IntI) = IntI(width/other.width, height/other.height)

    operator fun plus(other: IntI) = IntI(width+other.width, height+other.height)

    operator fun minus(other: IntI) = IntI(width-other.width, height-other.height)

    operator fun times(other: Int) = IntI(width*other, height*other)
    operator fun div(other: Int) = IntI(width/other, height/other)

}
