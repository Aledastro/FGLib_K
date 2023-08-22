package com.uzery.fglib.utils.math.geom

data class LineN(private val pos: PointN, private val size: PointN): Shape() {
    constructor(size: PointN): this(PointN.ZERO, size)

    override fun copy(move: PointN) = LineN(pos+move, size)

    override fun into(pos: PointN): Boolean {
        return this.pos.lengthTo(pos) < size[0]/2 //todo
    }

    override val L = pos-size/2
    override val R = pos+size/2

    override val code = Code.OVAL

    override fun toString(): String {
        return "line[$pos, $size]"
    }

    companion object {
        fun LR(l: PointN, r: PointN) = LineN(l, r-l)

        fun L(l: PointN, s: PointN) = LineN(l, s)
        fun C(c: PointN, s: PointN) = LineN(c-s/2, s)
        fun R(r: PointN, s: PointN) = LineN(r-s, s)

        fun L(s: PointN) = L(PointN.ZERO, s)
        fun C(s: PointN) = C(PointN.ZERO, s)
        fun R(s: PointN) = R(PointN.ZERO, s)
    }
}
