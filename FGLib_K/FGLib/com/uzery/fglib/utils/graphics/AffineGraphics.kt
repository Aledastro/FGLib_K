package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.math.geom.PointN

abstract class AffineGraphics {
    var drawPOS = PointN.ZERO
    var layer = DrawLayer.CAMERA_OFF

    abstract fun setStroke(size: Double)

    abstract val image: ImageGraphics

    abstract val fill: GeometryGraphics
    abstract val stroke: GeometryGraphics
}
