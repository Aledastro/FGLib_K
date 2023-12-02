package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.math.geom.PointN

abstract class AffineGraphics {
    var alpha = 1.0
    var view_scale = 1.0
    var drawPOS = PointN.ZERO
    var layer = DrawLayer.CAMERA_OFF

    abstract fun setStroke(size: Double)
    fun setDefaults() {
        alpha = 1.0
        view_scale = 1.0
        layer = DrawLayer.CAMERA_OFF
        image.setDefaults()
        fill.setDefaults()
        stroke.setDefaults()
    }

    abstract val image: ImageGraphics

    abstract val fill: GeometryGraphics
    abstract val stroke: GeometryGraphics
}
