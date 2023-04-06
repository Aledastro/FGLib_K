package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

abstract class GeometryGraphics {
    abstract var color: Paint
    var transform = AffineTransform { p -> p }

    abstract fun rect(pos: PointN, size: PointN)

    abstract fun oval(pos: PointN, size: PointN)

    fun rect(pos: PointN, size: PointN, color: Color) {
        this.color = color
        rect(transform.pos(pos), transform.size(pos, size))
    }

    fun oval(pos: PointN, size: PointN, color: Color) {
        this.color = color
        oval(pos, size)
    }
}
