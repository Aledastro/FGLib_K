package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.graphics.data.FGFontPosture
import com.uzery.fglib.utils.graphics.data.FGFontWeight
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.FigureN

abstract class GeometryGraphics(private val transform: AffineTransform, private val transformSize: AffineTransform) {
    var alpha = 1.0
    fun setDefaults() {
        font()
        alpha = 1.0
    }

    val font: FGFont
        get() = FGFont(font_family, font_size, font_weight, font_posture).resize(
            transformSize.transform(
                PointN(
                    font_size
                )
            ).X
        )

    fun font(
        family: String = "arial", size: Double = 10.0,
        weight: FGFontWeight = FGFontWeight.NORMAL, posture: FGFontPosture = FGFontPosture.REGULAR
    ) {
        font_family = family
        font_size = size
        font_weight = weight
        font_posture = posture
    }

    fun text_size(text: String): PointN {
        return Platform.realisation.text_size(text, font)
    }

    var font_family = "arial"
    var font_size = 10.0
    var font_weight = FGFontWeight.NORMAL
    var font_posture = FGFontPosture.REGULAR

    protected abstract fun rect0(pos: PointN, size: PointN, color: FGColor)

    protected abstract fun oval0(pos: PointN, size: PointN, color: FGColor)

    protected abstract fun line0(pos1: PointN, pos2: PointN, color: FGColor)

    protected abstract fun text0(pos: PointN, text: String, color: FGColor)

    ///////////////////////////////////////////////////////////////////////////

    fun rect(pos: PointN, size: PointN, color: FGColor) {
        rect0(transform.pos(pos), transform.size(pos, size), color)
    }

    fun rectL(pos: PointN, size: PointN, color: FGColor) = rect(pos, size, color)
    fun rectC(pos: PointN, size: PointN, color: FGColor) = rect(pos-size/2, size, color)
    fun rectR(pos: PointN, size: PointN, color: FGColor) = rect(pos-size, size, color)

    ///////////////////////////////////////////////////////////////////////////

    fun oval(pos: PointN, size: PointN, color: FGColor) {
        oval0(transform.pos(pos), transform.size(pos, size), color)
    }

    fun ovalL(pos: PointN, size: PointN, color: FGColor) = oval(pos, size, color)

    fun ovalC(pos: PointN, size: PointN, color: FGColor) = oval(pos-size/2, size, color)

    fun ovalR(pos: PointN, size: PointN, color: FGColor) = oval(pos-size, size, color)

    ///////////////////////////////////////////////////////////////////////////

    fun text(pos: PointN, text: String, color: FGColor) {
        text0(transform.pos(pos), text, color)
    }

    fun textL(pos: PointN, text: String, color: FGColor) = text(pos, text, color)

    fun textC(pos: PointN, text: String, color: FGColor) = text(pos-text_size(text).XP/2, text, color)

    fun textR(pos: PointN, text: String, color: FGColor) = text(pos-text_size(text).XP, text, color)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN, shape: Shape, color: FGColor) {
        when (shape.code) {
            Shape.Code.RECT -> rect(pos+shape.L, shape.S, color)
            Shape.Code.OVAL -> oval(pos+shape.L, shape.S, color)
            Shape.Code.FIGURE -> {
                val fig = shape as FigureN
                if (shape.exists()) {
                    for (field in fig.fields) {
                        val p = PointN(Array(field.dim) { field.mirage[it, 0] })
                        line(pos, p, color)
                    }
                    for (p in fig.current_pos) {
                        oval(pos-PointN(1, 1)+p, PointN(2, 2), color)
                    }
                }
            }
        }
    }

    fun draw(shape: Shape, color: FGColor) = draw(PointN.ZERO, shape, color)

    fun drawL(pos: PointN, shape: Shape, color: FGColor) = draw(pos, shape, color)
    fun drawC(pos: PointN, shape: Shape, color: FGColor) = draw(pos-shape.S/2, shape, color)
    fun drawR(pos: PointN, shape: Shape, color: FGColor) = draw(pos-shape.S, shape, color)

    ///////////////////////////////////////////////////////////////////////////

    fun lineTo(pos1: PointN, pos2: PointN, color: FGColor) {
        line0(transform.pos(pos1), transform.pos(pos2), color)
    }

    fun line(pos: PointN, size: PointN, color: FGColor) {
        line0(transform.pos(pos), transform.pos(pos+size), color)
    }
}
