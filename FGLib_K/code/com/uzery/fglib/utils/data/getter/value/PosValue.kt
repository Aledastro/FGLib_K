package com.uzery.fglib.utils.data.getter.value

import com.uzery.fglib.utils.math.geom.PointN

data class PosValue(val input: PointN): ObjectValue {
    override fun toString(): String {
        if (input == PointN.ZERO) return "pos[ZERO]"

        val b = StringBuilder("pos[${input[0].toInt()}")
        for (i in 1 until input.dim) b.append(", ${input[i].toInt()}")
        return b.append("]").toString()
    }
}
