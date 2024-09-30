package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.ShapeUtils.getFields
import com.uzery.fglib.utils.math.geom.PointN

class FigureRectN(pos: PointN, val size: PointN): FigureN(getFields(pos, size)) {
    constructor(rect: RectN): this(rect.C, rect.S)

    companion object {
        fun LR(l: PointN, r: PointN) = FigureRectN(l, r-l)

        fun L(l: PointN = PointN.ZERO, s: PointN) = FigureRectN(l+s/2, s)
        fun C(c: PointN = PointN.ZERO, s: PointN) = FigureRectN(c, s)
        fun R(r: PointN = PointN.ZERO, s: PointN) = FigureRectN(r-s/2, s)

        fun L(s: PointN) = L(PointN.ZERO, s)
        fun C(s: PointN) = C(PointN.ZERO, s)
        fun R(s: PointN) = R(PointN.ZERO, s)
    }
}
