package com.uzery.fglib.utils.math.getter.value

import com.uzery.fglib.utils.math.geom.PointN

data class SizeValue(val input: PointN): ObjectValue {
    override fun toString(): String {
        val b = StringBuilder("size[")
        b.append(input[0].toInt())
        (1 until input.dimension()).forEach { i -> b.append(", ").append(input[i].toInt()) }
        return b.append("]").toString()
    }
}