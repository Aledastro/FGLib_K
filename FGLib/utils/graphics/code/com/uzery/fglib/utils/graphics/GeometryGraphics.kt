package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

abstract class GeometryGraphics(private val transform: AffineTransform) {
    abstract var color: Paint

    abstract fun rect(pos: PointN, size: PointN)

    abstract fun oval(pos: PointN, size: PointN)

    abstract fun text(pos: PointN, text: String)

    fun rect(pos: PointN, size: PointN, color: Color) {
        this.color = color
        rect(transform.pos(pos), transform.size(pos, size))
    }

    fun oval(pos: PointN, size: PointN, color: Color) {
        this.color = color
        oval(pos, size)
    }

    fun text(pos: PointN, text: String, color: Color) {
        this.color = color
        text(pos, text)
    }
}
