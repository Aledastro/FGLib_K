package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.FGLibSettings.OPTIMISATION_NOT_DRAW_OUT_OF_BOUNDS
import com.uzery.fglib.core.program.Platform.CANVAS_REAL
import com.uzery.fglib.core.program.Platform.scale
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class AffineGraphics {
    private val DEFAULT_ROTATE = 0.0
    private val DEFAULT_VIEW_SCALE = 1.0
    private val DEFAULT_ALPHA = 1.0

    var global_rotate = DEFAULT_ROTATE
    var global_view_scale = DEFAULT_VIEW_SCALE
    var global_alpha = DEFAULT_ALPHA

    var global_transform = AffineTransform.NEUTRAL

    var rotate = DEFAULT_ROTATE
    var view_scale = DEFAULT_VIEW_SCALE
    var alpha = DEFAULT_ALPHA

    var drawPOS = PointN.ZERO
    var layer = DrawLayer.CAMERA_OFF

    var whole_draw = true

    ////////////////////////////////////////////////////////////////////////////////////////

    val default_transform: AffineTransform

    init {
        val default_t: (PointN) -> PointN = { p ->
            var x = (p-drawPOS*layer.z)*scale
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

        global_rotate = DEFAULT_ROTATE
        global_view_scale = DEFAULT_VIEW_SCALE
        global_alpha = DEFAULT_ALPHA

        global_transform = AffineTransform.NEUTRAL
    }

    fun setDefaults() {
        rotate = DEFAULT_ROTATE
        view_scale = DEFAULT_VIEW_SCALE
        alpha = DEFAULT_ALPHA

        layer = DrawLayer.CAMERA_OFF
        drawPOS = PointN.ZERO

        fill.setDefaults()
        stroke.setDefaults()
        image.setDefaults()
    }

    abstract fun text_size(text: String, font: FGFont): PointN

    abstract val image: ImageGraphics

    abstract val fill: FillGraphics
    abstract val stroke: StrokeGraphics

    internal fun isOutOfBounds(pos: PointN, size: PointN): Boolean {
        if (!OPTIMISATION_NOT_DRAW_OUT_OF_BOUNDS) return false

        val pos1 = global_transform.pos(pos)
        val pos2 = global_transform.pos(pos+size)
        for (i in 0..<pos1.dim) {
            val list = listOf(pos1[i], pos2[i])

            if (list.all { it < 0 } || list.all { it > CANVAS_REAL[i] }) {
                return true
            }
        }
        return false
    }

    internal fun getAlpha(): Double {
        return (global_alpha*this.alpha).coerceIn(0.0, 1.0)
    }

    internal fun getAlphaColor(c: FGColor): FGColor {
        return c.transparent(getAlpha())
    }
}
