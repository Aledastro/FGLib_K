package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

data class OvalN(private val pos: PointN, private val size: PointN): Shape() {
    //todo OvalN is CircleN just now
    constructor(size: PointN): this(PointN.ZERO, size)

    override fun copy(move: PointN) = OvalN(pos+move, size)

    override fun into(pos: PointN): Boolean {
        return C.lengthTo(pos) < size[0]/2 //todo
    }

    override val L = pos-size/2
    override val R = pos+size/2

    override val code = Code.OVAL

    override fun toString(): String {
        return "oval[$pos, $size]"
    }

    companion object {
        fun LR(l: PointN, r: PointN) = OvalN(l, r-l)
        fun L(l: PointN = PointN.ZERO, s: PointN) = OvalN(l, s)
        fun C(c: PointN = PointN.ZERO, s: PointN) = OvalN(c-s/2, s)
        fun R(r: PointN = PointN.ZERO, s: PointN) = OvalN(r-s, s)

        fun L(s: PointN) = L(PointN.ZERO, s)
        fun C(s: PointN) = C(PointN.ZERO, s)
        fun R(s: PointN) = R(PointN.ZERO, s)
    }
}
