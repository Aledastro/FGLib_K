package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.PointN
import java.util.LinkedList

class FigureRectN(pos: PointN, val size: PointN): FigureN(pos+size/2, getFields(size)) {
    constructor(rect: RectN): this(rect.L, rect.S)
}

fun getFields(size: PointN): List<FieldN> {
    val fields = LinkedList<FieldN>()
    val dim = size.dim

    for (level in 0 until dim) {
        val xs = Array(dim) { i -> if (i == level) size[i]/2 else 0.0 }
        val field = FieldN(PointN.ZERO, PointN(xs))
        fields.add(field)
        fields.add(-field)
    }

    return fields
}
