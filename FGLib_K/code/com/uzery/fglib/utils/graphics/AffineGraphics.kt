package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Program
import com.uzery.fglib.utils.math.geom.PointN

abstract class AffineGraphics {
    var drawPOS = PointN.ZERO
    var layer = DrawLayer.CAMERA_OFF
    var alpha = 1.0
        set(input) {
            Program.gc.globalAlpha = input*Platform.global_alpha
            field = input
        }

    abstract fun setStroke(size: Double)
    fun setDefaults() {
        alpha = 1.0
        layer = DrawLayer.CAMERA_OFF
        image.setDefaults()
        fill.setDefaults()
        stroke.setDefaults()
    }

    abstract val image: ImageGraphics

    abstract val fill: GeometryGraphics
    abstract val stroke: GeometryGraphics
}
