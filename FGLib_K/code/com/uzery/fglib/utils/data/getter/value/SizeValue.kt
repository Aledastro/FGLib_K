package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.utils.math.geom.PointN

data class SizeValue(val input: PointN): ObjectValue {
    override fun toString(): String {
        if (input == PointN.ZERO) return "pos[ZERO]"

        return buildString {
            append("size[")
            append(input[0].toInt())
            for (i in 1..<input.dim) {
                append(", ")
                append(input[i].toInt())
            }
            append("]")
        }
    }
}
