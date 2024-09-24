package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN

interface RenderCamera {
    operator fun get(p: PointN): PointN
    fun sort(p1: PointN, p2: PointN): Int
}
