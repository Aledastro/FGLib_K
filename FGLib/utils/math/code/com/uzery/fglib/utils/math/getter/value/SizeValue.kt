package com.uzery.fglib.utils.math.getter.value

import com.uzery.fglib.utils.math.geom.PointN

data class SizeValue(val input: PointN): ObjectValue {
    override fun toString(): String {
        val b = StringBuilder("size[")
        b.append(input.get(0).toInt())
        for(i in 1 until input.dimension()) {
            b.append(", ").append(input.get(i).toInt())
        }
        return b.append("]").toString()
    }
}