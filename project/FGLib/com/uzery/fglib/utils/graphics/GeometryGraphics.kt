package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.text.Font
import javafx.scene.text.TextAlignment

abstract class GeometryGraphics(private val transform: AffineTransform) {
    var font = Font.font(10.0)
    protected var text_align = TextAlignment.RIGHT
    abstract var color: Paint

    protected abstract fun rect0(pos: PointN, size: PointN)

    protected abstract fun oval0(pos: PointN, size: PointN)

    protected abstract fun line0(pos1: PointN, pos2: PointN)

    protected abstract fun text0(pos: PointN, text: String)

    fun rect(pos: PointN, size: PointN, color: Color) {
        this.color = color
        rect0(transform.pos(pos), transform.size(pos, size))
    }

    fun oval(pos: PointN, size: PointN, color: Color) {
        this.color = color
        oval0(transform.pos(pos), transform.size(pos, size))
    }

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

    fun draw(pos: PointN, shape: Shape, color: Color) {
        when(shape.code) {
            Shape.Code.RECT -> rect(pos + shape.L, shape.S, color)
            Shape.Code.OVAL -> oval(pos + shape.L, shape.S, color)
        }
    }

    fun lineTo(pos1: PointN, pos2: PointN, color: Color) {
        this.color = color
        line0(transform.pos(pos1), transform.pos(pos2))
    }
    fun line(pos: PointN, size: PointN, color: Color) {
        this.color = color
        line0(transform.pos(pos), pos+transform.size(pos,size))
    }

    fun draw(shape: Shape, color: Color) = draw(PointN.ZERO, shape, color)
}
