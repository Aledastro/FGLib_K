package com.uzery.fglib.utils.math.matrix

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.struct.Array2

object MatrixUtils {
    fun toArray(vararg data: PointN): Array2<Double> {
        val dim = data[0].dim
        if (data.any { p -> p.dim != dim }) throw DebugData.error("ERROR: wrong dim: $data")
        val res = Array2(dim, data.size) { 0.0 }
        res.set { i, j -> data[j][i] }
        return res
    }
}
