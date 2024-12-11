package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
data class PosValue(val input: PointN): ObjectValue {
    override fun toString(): String {
        if (input == PointN.ZERO) return "pos[ZERO]"

        return buildString {
            append("pos[")
            append(input[0].toInt())
            for (i in 1..<input.dim) {
                append(", ")
                append(input[i].toInt())
            }
            append("]")
        }
    }
}
