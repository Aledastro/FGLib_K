package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.FieldN
import com.uzery.fglib.utils.math.geom.PointN
import java.util.LinkedList

class FigureRectN(pos: PointN, val size: PointN): FigureN(getFields(pos, size)) {
    constructor(rect: RectN): this(rect.L, rect.S)

    companion object{
        fun LR(l: PointN, r: PointN) = FigureRectN(l, r-l)

        fun L(l: PointN = PointN.ZERO, s: PointN) = FigureRectN(l+s/2, s)
        fun C(c: PointN = PointN.ZERO, s: PointN) = FigureRectN(c, s)
        fun R(r: PointN = PointN.ZERO, s: PointN) = FigureRectN(r-s/2, s)

        fun L(s: PointN) = L(PointN.ZERO, s)
        fun C(s: PointN) = C(PointN.ZERO, s)
        fun R(s: PointN) = R(PointN.ZERO, s)
    }
}

fun getFields(pos: PointN, size: PointN): List<FieldN> {
    val fields = LinkedList<FieldN>()
    val dim = size.dim

    for (level in 0 until dim) {
        val xs = Array(dim) { i -> if (i == level) size[i]/2 else 0.0 }
        val field = FieldN(PointN.ZERO, PointN(xs))
        fields.add(field.copy(pos))
        fields.add((-field).copy(pos))
    }

    return fields
}
