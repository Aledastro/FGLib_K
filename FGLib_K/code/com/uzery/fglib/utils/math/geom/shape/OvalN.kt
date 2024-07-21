package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

data class OvalN(private val pos: PointN, private val size: PointN): Shape() {
    constructor(size: PointN): this(PointN.ZERO, size)

    override fun copy(move: PointN) = OvalN(pos+move, size)

    override fun into(pos: PointN): Boolean {
        val st_pos = pos/size
        val st_oval = OvalN(this.pos/size, PointN(1, 1))
        return st_oval.C.lengthTo(st_pos) < 0.5
    }

    override val L = pos
    override val R = pos+size

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
