package com.uzery.fglib.utils.math.geom

data class RectN(private val pos: PointN, private val size: PointN): Shape() {
    override fun copy(move: PointN): Shape {
        return RectN(pos + move, size)
    }

    override val L = pos
    override val R = pos + size

    override val code = Code.RECT

    override fun toString(): String {
        return "rect[$pos, $size]"
    }

    companion object {
        fun rectLR(l: PointN, r: PointN) = RectN(l, r - l)
    }
}
