package com.uzery.fglib.utils.math.geom

class RectN(private val pos: PointN, private val size: PointN): Shape() {
    override val L: PointN
        get() = pos
    override val R: PointN
        get() = pos + size


}
