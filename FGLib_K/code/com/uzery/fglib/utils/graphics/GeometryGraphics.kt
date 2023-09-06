package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.FigureN
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment

abstract class GeometryGraphics(private val transform: AffineTransform) {
    fun setDefaults() {
        font()
        text_align = TextAlignment.RIGHT
        this.color = Color.BLACK
    }

    fun font(
        family: String = "arial", size: Double = 10.0,
        weight: FontWeight = FontWeight.NORMAL, posture: FontPosture = FontPosture.REGULAR
    ) {
        font_family = family
        font_size = size
        font_weight = weight
        font_posture = posture
    }

    var font_family = "arial"
    var font_size = 10.0
    var font_weight = FontWeight.NORMAL
    var font_posture = FontPosture.REGULAR

    protected var text_align = TextAlignment.RIGHT
    abstract var color: Paint

    protected abstract fun rect0(pos: PointN, size: PointN)

    protected abstract fun oval0(pos: PointN, size: PointN)

    protected abstract fun line0(pos1: PointN, pos2: PointN)

    protected abstract fun text0(pos: PointN, text: String)

    ///////////////////////////////////////////////////////////////////////////

    fun rect(pos: PointN, size: PointN, color: Color) {
        this.color = color
        rect0(transform.pos(pos), transform.size(pos, size))
    }

    fun rectL(pos: PointN, size: PointN, color: Color) = rect(pos, size, color)
    fun rectC(pos: PointN, size: PointN, color: Color) = rect(pos-size/2, size, color)
    fun rectR(pos: PointN, size: PointN, color: Color) = rect(pos-size, size, color)

    ///////////////////////////////////////////////////////////////////////////

    fun oval(pos: PointN, size: PointN, color: Color) {
        this.color = color
        oval0(transform.pos(pos), transform.size(pos, size))
    }

    fun ovalL(pos: PointN, size: PointN, color: Color) = oval(pos, size, color)

    fun ovalC(pos: PointN, size: PointN, color: Color) = oval(pos-size/2, size, color)

    fun ovalR(pos: PointN, size: PointN, color: Color) = oval(pos-size, size, color)

    ///////////////////////////////////////////////////////////////////////////

    fun text(pos: PointN, text: String, color: Color) {
        this.color = color
        this.text_align = TextAlignment.LEFT
        text0(transform.pos(pos), text)
    }

    fun textL(pos: PointN, text: String, color: Color) {
        this.color = color
        this.text_align = TextAlignment.LEFT
        text0(transform.pos(pos), text)
    }

    fun textC(pos: PointN, text: String, color: Color) {
        this.color = color
        this.text_align = TextAlignment.CENTER
        text0(transform.pos(pos), text)
    }

    fun textR(pos: PointN, text: String, color: Color) {
        this.color = color
        this.text_align = TextAlignment.RIGHT
        text0(transform.pos(pos), text)
    }

    ///////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN, shape: Shape, color: Color) {
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

    fun drawL(pos: PointN, shape: Shape, color: Color) = draw(pos, shape, color)
    fun drawC(pos: PointN, shape: Shape, color: Color) = draw(pos-shape.S/2, shape, color)
    fun drawR(pos: PointN, shape: Shape, color: Color) = draw(pos-shape.S, shape, color)

    fun draw(shape: Shape, color: Color) = draw(PointN.ZERO, shape, color)

    ///////////////////////////////////////////////////////////////////////////

    fun lineTo(pos1: PointN, pos2: PointN, color: Color) {
        this.color = color
        line0(transform.pos(pos1), transform.pos(pos2))
    }

    fun line(pos: PointN, size: PointN, color: Color) {
        this.color = color
        line0(transform.pos(pos), transform.pos(pos)+transform.size(pos, size))
    }
}
