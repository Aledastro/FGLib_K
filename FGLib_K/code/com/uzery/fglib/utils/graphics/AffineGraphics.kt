package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN

abstract class AffineGraphics {
    var global_alpha = 1.0
    var alpha = 1.0
    var view_scale = 1.0
    var drawPOS = PointN.ZERO
    var layer = DrawLayer.CAMERA_OFF

    abstract var scale: Int
    abstract var lineWidth: Double
    abstract var lineDashOffset: Double
    abstract fun setLineDashes(vararg dashes: Double)

    abstract var global_view_scale: Double
    abstract var whole_draw: Boolean

    abstract fun setStroke(size: Double)
    fun setDefaults() {
        alpha = 1.0
        view_scale = 1.0
        layer = DrawLayer.CAMERA_OFF
        image.setDefaults()
        fill.setDefaults()
        stroke.setDefaults()
    }

    abstract fun text_size(text: String, font: FGFont): PointN

    abstract val image: ImageGraphics

    abstract val fill: GeometryGraphics
    abstract val stroke: GeometryGraphics
}
