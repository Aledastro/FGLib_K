package com.uzery.fglib.utils.math.getter.value

import com.uzery.fglib.utils.math.geom.PointN

data class SizeValue(val input: PointN): ObjectValue {
    override fun toString(): String {
        val b = StringBuilder("size[${input[0].toInt()}")
        for(i in 1 until input.dimension()) b.append(", ${input[i].toInt()}")
        return b.append("]").toString()
    }
}