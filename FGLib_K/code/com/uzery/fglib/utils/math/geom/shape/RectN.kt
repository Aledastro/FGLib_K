package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

/**
 * TODO("doc")
 **/
data class RectN(val pos: PointN, val size: PointN): Shape() {
    constructor(size: PointN): this(PointN.ZERO, size)

    override fun copy(move: PointN) = RectN(pos+move, size)

    override fun into(pos: PointN): Boolean {
        return L.less(pos) && pos.less(R)
    }

    override val L = pos
    override val R = pos+size

    override val code = Code.RECT

    override fun toString(): String {
        return "rect[$pos, $size]"
    }

    companion object {
        fun LR(l: PointN, r: PointN) = RectN(l, r-l)

        fun L(l: PointN = PointN.ZERO, s: PointN) = RectN(l, s)
        fun C(c: PointN = PointN.ZERO, s: PointN) = RectN(c-s/2, s)
        fun R(r: PointN = PointN.ZERO, s: PointN) = RectN(r-s, s)

        fun L(s: PointN) = L(PointN.ZERO, s)
        fun C(s: PointN) = C(PointN.ZERO, s)
        fun R(s: PointN) = R(PointN.ZERO, s)
    }
}
