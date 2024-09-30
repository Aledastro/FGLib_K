package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.FigureN

abstract class GeometryGraphics(protected val agc: AffineGraphics) {
    protected val transform
        get() = agc.transform

    var alpha = 1.0

    open fun setDefaults() {
        alpha = 1.0
    }

    protected abstract fun rect0(pos: PointN, size: PointN, color: FGColor)

    protected abstract fun oval0(pos: PointN, size: PointN, color: FGColor)

    ///////////////////////////////////////////////////////////////////////////

    fun rect(pos: PointN, size: PointN, color: FGColor) {
        if (agc.isOutOfBounds(pos, size)) return

        agc.applyAlphaWith(alpha)
        rect0(transform.pos(pos), transform.size(pos, size), color)
    }

    fun rectL(pos: PointN, size: PointN, color: FGColor) = rect(pos, size, color)
    fun rectC(pos: PointN, size: PointN, color: FGColor) = rect(pos-size/2, size, color)
    fun rectR(pos: PointN, size: PointN, color: FGColor) = rect(pos-size, size, color)

    ///////////////////////////////////////////////////////////////////////////

    fun oval(pos: PointN, size: PointN, color: FGColor) {
        if (agc.isOutOfBounds(pos, size)) return

        agc.applyAlphaWith(alpha)
        oval0(transform.pos(pos), transform.size(pos, size), color)
    }

    fun ovalL(pos: PointN, size: PointN, color: FGColor) = oval(pos, size, color)

    fun ovalC(pos: PointN, size: PointN, color: FGColor) = oval(pos-size/2, size, color)

    fun ovalR(pos: PointN, size: PointN, color: FGColor) = oval(pos-size, size, color)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN, shape: Shape, color: FGColor) {
        when (shape.code) {
            Shape.Code.RECT -> rect(pos+shape.L, shape.S, color)
            Shape.Code.OVAL -> oval(pos+shape.L, shape.S, color)
            Shape.Code.FIGURE -> {
                agc.applyAlphaWith(alpha)
                //todo out of bounds for Figure

                val fig = shape as FigureN
                if (shape.exists()) {
                    for (field in fig.fields) {
                        val p = PointN(Array(field.dim) { field.mirage[it, 0] })
                        agc.stroke.line(pos, p, color)
                    }
                    for (p in fig.current_pos) {
                        oval(pos-PointN(1, 1)+p, PointN(2, 2), color)
                    }
                }
            }
        }
    }

    fun drawL(pos: PointN, shape: Shape, color: FGColor) = draw(pos, shape, color)
    fun drawC(pos: PointN, shape: Shape, color: FGColor) = draw(pos-shape.S/2, shape, color)
    fun drawR(pos: PointN, shape: Shape, color: FGColor) = draw(pos-shape.S, shape, color)

    ///////////////////////////////////////////////////////////////////////////

    fun draw(shape: Shape, color: FGColor) = draw(PointN.ZERO, shape, color)
    fun drawL(shape: Shape, color: FGColor) = draw(PointN.ZERO, shape, color)
    fun drawC(shape: Shape, color: FGColor) = draw(-shape.S/2, shape, color)
    fun drawR(shape: Shape, color: FGColor) = draw(-shape.S, shape, color)
}
