package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.FigureN
import com.uzery.fglib.utils.math.geom.shape.OvalN
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * TODO("doc")
 **/
abstract class GeometryGraphics(protected val agc: AffineGraphics) {
    protected val transform
        get() = agc.global_transform

    protected abstract fun rect0(pos: PointN, size: PointN, color: FGColor)

    protected abstract fun oval0(pos: PointN, size: PointN, color: FGColor)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun rect(pos: PointN, size: PointN, color: FGColor) {
        if (agc.isOutOfBounds(pos, size)) return

        rect0(transform.pos(pos), transform.size(pos, size), agc.getAlphaColor(color))
    }

    fun oval(pos: PointN, size: PointN, color: FGColor) {
        if (agc.isOutOfBounds(pos, size)) return

        oval0(transform.pos(pos), transform.size(pos, size), agc.getAlphaColor(color))
    }

    fun figure(pos: PointN, figure: FigureN, color: FGColor) {
        if (!figure.exists()) return

        val c = agc.getAlphaColor(color)

        for (field in figure.fields) {
            val p = PointN(Array(field.dim) { field.mirage[it, 0] })
            agc.stroke.line(pos, p, c)
        }
        for (p in figure.current_pos) {
            oval(pos-PointN(1, 1)+p, PointN(2, 2), c)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN, shape: Shape, color: FGColor) {
        when (shape.code) {
            Shape.Code.RECT -> rect(pos+shape.L, shape.S, color)
            Shape.Code.OVAL -> oval(pos+shape.L, shape.S, color)
            Shape.Code.FIGURE -> figure(pos, shape as FigureN, color)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun rectL(pos: PointN, size: PointN, color: FGColor) = rect(pos, size, color)
    fun rectC(pos: PointN, size: PointN, color: FGColor) = rect(pos-size/2, size, color)
    fun rectR(pos: PointN, size: PointN, color: FGColor) = rect(pos-size, size, color)

    fun rect(pos: PointN, rect: RectN, color: FGColor) = rect(pos+rect.pos, rect.size, color)
    fun rectL(pos: PointN, rect: RectN, color: FGColor) = rect(pos+rect.pos, rect.size, color)
    fun rectC(pos: PointN, rect: RectN, color: FGColor) = rect(pos+rect.pos-rect.size/2, rect.size, color)
    fun rectR(pos: PointN, rect: RectN, color: FGColor) = rect(pos+rect.pos-rect.size, rect.size, color)

    fun rect(rect: RectN, color: FGColor) = rect(rect.pos, rect.size, color)
    fun rectL(rect: RectN, color: FGColor) = rect(rect.pos, rect.size, color)
    fun rectC(rect: RectN, color: FGColor) = rect(rect.pos-rect.size/2, rect.size, color)
    fun rectR(rect: RectN, color: FGColor) = rect(rect.pos-rect.size, rect.size, color)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun ovalL(pos: PointN, size: PointN, color: FGColor) = oval(pos, size, color)
    fun ovalC(pos: PointN, size: PointN, color: FGColor) = oval(pos-size/2, size, color)
    fun ovalR(pos: PointN, size: PointN, color: FGColor) = oval(pos-size, size, color)

    fun oval(pos: PointN, oval: OvalN, color: FGColor) = oval(pos+oval.pos, oval.size, color)
    fun ovalL(pos: PointN, oval: OvalN, color: FGColor) = oval(pos+oval.pos, oval.size, color)
    fun ovalC(pos: PointN, oval: OvalN, color: FGColor) = oval(pos+oval.pos-oval.size/2, oval.size, color)
    fun ovalR(pos: PointN, oval: OvalN, color: FGColor) = oval(pos+oval.pos-oval.size, oval.size, color)

    fun oval(oval: OvalN, color: FGColor) = oval(oval.pos, oval.size, color)
    fun ovalL(oval: OvalN, color: FGColor) = oval(oval.pos, oval.size, color)
    fun ovalC(oval: OvalN, color: FGColor) = oval(oval.pos-oval.size/2, oval.size, color)
    fun ovalR(oval: OvalN, color: FGColor) = oval(oval.pos-oval.size, oval.size, color)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun figure(figure: FigureN, color: FGColor) = figure(PointN.ZERO, figure, color)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun drawL(pos: PointN, shape: Shape, color: FGColor) = draw(pos, shape, color)
    fun drawC(pos: PointN, shape: Shape, color: FGColor) = draw(pos-shape.S/2, shape, color)
    fun drawR(pos: PointN, shape: Shape, color: FGColor) = draw(pos-shape.S, shape, color)

    fun draw(shape: Shape, color: FGColor) = draw(PointN.ZERO, shape, color)
    fun drawL(shape: Shape, color: FGColor) = draw(PointN.ZERO, shape, color)
    fun drawC(shape: Shape, color: FGColor) = draw(-shape.S/2, shape, color)
    fun drawR(shape: Shape, color: FGColor) = draw(-shape.S, shape, color)

    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
