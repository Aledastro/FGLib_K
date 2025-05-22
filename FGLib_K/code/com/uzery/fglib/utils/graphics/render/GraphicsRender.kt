package com.uzery.fglib.utils.graphics.render

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

class GraphicsRender(val pos: PointN, val size: PointN) {
    val main = RectN(pos, size)
    val center = (pos+size)/2
}
