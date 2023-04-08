package com.uzery.fglib.utils.math.value

import com.uzery.fglib.utils.math.geom.PointN

data class PosValue(val input: PointN): ObjectValue{
    override fun toString(): String {
        val b = StringBuilder("pos[")
        b.append(input.get(0).toInt())
        for(i in 1 until input.dimension()) {
            b.append(", ").append(input.get(i).toInt())
        }
        return b.append("]").toString()
    }
}