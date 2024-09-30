package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.FGLibSettings.OPTIMISATION_NOT_DRAW_OUT_OF_BOUNDS
import com.uzery.fglib.core.program.Platform.CANVAS_REAL
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN

abstract class AffineGraphics {
    var global_alpha = 1.0
    var alpha = 1.0

    var view_scale = 1.0

    abstract var scale: Int
    abstract var global_view_scale: Double

    var drawPOS = PointN.ZERO
    var layer = DrawLayer.CAMERA_OFF
    var transform = AffineTransform.NEUTRAL

    var whole_draw = true

    ////////////////////////////////////////////////////////////////////////////////////////

    val default_transform: AffineTransform

    init {
        val default_t: (PointN) -> PointN = {
            var x = (it-drawPOS*layer.z)*scale
            if (whole_draw) x = x.roundC(1.0)
            x *= view_scale*global_view_scale
            x
        }
        val default_t_size: (PointN) -> PointN = { x ->
            x*scale*view_scale*global_view_scale
        }
        default_transform = AffineTransform(default_t, default_t_size)
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    fun setFullDefaults() {
        setDefaults()

        drawPOS = PointN.ZERO
        transform = AffineTransform.NEUTRAL
        global_alpha = 1.0
        global_view_scale = 1.0

        fill.setFullDefaults()
        stroke.setFullDefaults()
        image.setFullDefaults()
    }

    fun setDefaults() {
        alpha = 1.0
        view_scale = 1.0
        layer = DrawLayer.CAMERA_OFF

        fill.setDefaults()
        stroke.setDefaults()
        image.setDefaults()
    }

    abstract fun text_size(text: String, font: FGFont): PointN

    abstract val image: ImageGraphics

    abstract val fill: FillGraphics
    abstract val stroke: StrokeGraphics

    protected abstract fun applyAlpha(alpha: Double)

    internal fun applyAlphaWith(alpha: Double) {
        applyAlpha((global_alpha*this.alpha*alpha).coerceIn(0.0, 1.0))
    }

    internal fun isOutOfBounds(pos: PointN, size: PointN): Boolean {
        if (!OPTIMISATION_NOT_DRAW_OUT_OF_BOUNDS) return false

        val pos1 = transform.pos(pos)
        val pos2 = transform.pos(pos+size)
        for (i in 0..<pos1.dim) {
            val list = listOf(pos1[i], pos2[i])

            if (list.all { it < 0 } || list.all { it > CANVAS_REAL[i] }) {
                return true
            }
        }
        return false
    }
}
