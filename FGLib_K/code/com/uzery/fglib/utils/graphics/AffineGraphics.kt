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
    var transform = AffineTransform.NEUTRAL

    abstract var scale: Int

    abstract var global_view_scale: Double
    var whole_draw = true

    /*private val transform =
        AffineTransform {
            var x = (it-drawPOS*layer.z)*scale
            x *= view_scale*global_view_scale
            x
        }
    private val whole_transform =
        AffineTransform {
            var x = (it-drawPOS*layer.z)*scale
            if (whole_draw) x = x.roundC(1.0)
            x *= view_scale*global_view_scale
            x
        }
    private val transformSize = AffineTransform { it*scale*view_scale*global_view_scale }*/

    fun setFullDefaults(){
        setDefaults()
        drawPOS = PointN.ZERO
        transform = AffineTransform.NEUTRAL

        val t: (PointN) -> PointN = {
            var x = (it-drawPOS*layer.z)*scale
            if (whole_draw) x = x.roundC(1.0)
            x *= view_scale*global_view_scale
            x
        }
        val t_size: (PointN) -> PointN = { x ->
            x*scale*view_scale*global_view_scale
        }

        transform = AffineTransform(t, t_size)
    }
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
    abstract val stroke: StrokeGraphics
}
