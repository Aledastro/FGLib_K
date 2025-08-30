package com.uzery.fglib.utils.graphics.sub

import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.OvalN
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * TODO("doc")
 **/
abstract class GeometryGraphics(protected val agc: AffineGraphics): SubGraphics() {
    protected val transform
        get() = agc.global_transform

    protected abstract fun renderRect(pos: PointN, size: PointN, color: FGColor)

    protected abstract fun renderOval(pos: PointN, size: PointN, color: FGColor)

    protected abstract fun renderPolygon(points: List<PointN>, color: FGColor)

    protected var OF_L = PointN(0.0, 0.0)
    protected var OF_C = PointN(0.5, 0.5)
    protected var OF_R = PointN(1.0, 1.0)

    protected var OF_TL = PointN(0.0, 0.0)
    protected var OF_TC = PointN(0.5, 0.0)
    protected var OF_TR = PointN(1.0, 0.0)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun rect(pos: PointN, size: PointN, color: FGColor, layout: PointN = OF_L) {
        val layout_pos = pos-size*layout
        val layout_size = size

        if (agc.isOutOfBounds(layout_pos, layout_size)) return

        renderRect(
            pos = transform.pos(layout_pos),
            size = transform.size(layout_pos, layout_size),
            color = agc.getAlphaColor(color)
        )
    }

    fun oval(pos: PointN, size: PointN, color: FGColor, layout: PointN = OF_L) {
        val layout_pos = pos-size*layout
        val layout_size = size

        if (agc.isOutOfBounds(layout_pos, layout_size)) return

        renderOval(
            pos = transform.pos(layout_pos),
            size = transform.size(layout_pos, layout_size),
            color = agc.getAlphaColor(color)
        )
    }

    protected fun polyTransform(pos: PointN, points: List<PointN>, layout: PointN): List<PointN>? {
        if (points.size < 3) return null

        val minP = PointN.create(2) { i -> points.minOf { p -> p[i] } }
        val maxP = PointN.create(2) { i -> points.maxOf { p -> p[i] } }
        val size = maxP-minP
        val layout_pos = pos-size*layout

        if (agc.isOutOfBounds(layout_pos+minP, size)) return null

        return points.map { p -> transform.pos(p+layout_pos) }
    }

    fun polygon(pos: PointN, points: List<PointN>, color: FGColor, layout: PointN = OF_L) {
        val transformed_points = polyTransform(pos, points, layout) ?: return
        renderPolygon(transformed_points, agc.getAlphaColor(color))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(pos: PointN, shape: Shape, color: FGColor, layout: PointN = OF_L) {
        when (shape.code) {
            Shape.Code.RECT -> rect(pos+shape.L, shape.S, color, layout)
            Shape.Code.OVAL -> oval(pos+shape.L, shape.S, color, layout)
            Shape.Code.FIGURE -> return
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun rectL(pos: PointN, size: PointN, color: FGColor) = rect(pos, size, color, layout = OF_L)
    fun rectC(pos: PointN, size: PointN, color: FGColor) = rect(pos, size, color, layout = OF_C)
    fun rectR(pos: PointN, size: PointN, color: FGColor) = rect(pos, size, color, layout = OF_R)

    fun rect(rect: RectN, color: FGColor) = rect(rect.pos, rect.size, color)
    fun rectL(rect: RectN, color: FGColor) = rect(rect.pos, rect.size, color, layout = OF_L)
    fun rectC(rect: RectN, color: FGColor) = rect(rect.pos, rect.size, color, layout = OF_C)
    fun rectR(rect: RectN, color: FGColor) = rect(rect.pos, rect.size, color, layout = OF_R)

    fun rect(pos: PointN, rect: RectN, color: FGColor) = rect(pos = pos+rect.pos, rect.size, color)
    fun rectL(pos: PointN, rect: RectN, color: FGColor) = rect(pos = pos+rect.pos, rect.size, color, layout = OF_L)
    fun rectC(pos: PointN, rect: RectN, color: FGColor) = rect(pos = pos+rect.pos, rect.size, color, layout = OF_C)
    fun rectR(pos: PointN, rect: RectN, color: FGColor) = rect(pos = pos+rect.pos, rect.size, color, layout = OF_R)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun ovalL(pos: PointN, size: PointN, color: FGColor) = oval(pos, size, color, layout = OF_L)
    fun ovalC(pos: PointN, size: PointN, color: FGColor) = oval(pos, size, color, layout = OF_C)
    fun ovalR(pos: PointN, size: PointN, color: FGColor) = oval(pos, size, color, layout = OF_R)

    fun oval(oval: OvalN, color: FGColor) = oval(oval.pos, oval.size, color)
    fun ovalL(oval: OvalN, color: FGColor) = oval(oval.pos, oval.size, color, layout = OF_L)
    fun ovalC(oval: OvalN, color: FGColor) = oval(oval.pos, oval.size, color, layout = OF_C)
    fun ovalR(oval: OvalN, color: FGColor) = oval(oval.pos, oval.size, color, layout = OF_R)

    fun oval(pos: PointN, oval: OvalN, color: FGColor) = oval(pos = pos+oval.pos, oval.size, color)
    fun ovalL(pos: PointN, oval: OvalN, color: FGColor) = oval(pos = pos+oval.pos, oval.size, color, layout = OF_L)
    fun ovalC(pos: PointN, oval: OvalN, color: FGColor) = oval(pos = pos+oval.pos, oval.size, color, layout = OF_C)
    fun ovalR(pos: PointN, oval: OvalN, color: FGColor) = oval(pos = pos+oval.pos, oval.size, color, layout = OF_R)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun polygonL(pos: PointN, points: List<PointN>, color: FGColor) = polygon(pos, points, color, layout = OF_L)
    fun polygonC(pos: PointN, points: List<PointN>, color: FGColor) = polygon(pos, points, color, layout = OF_C)
    fun polygonR(pos: PointN, points: List<PointN>, color: FGColor) = polygon(pos, points, color, layout = OF_R)

    fun polygon(points: List<PointN>, color: FGColor) = polygon(pos = PointN.ZERO, points, color)
    fun polygonL(points: List<PointN>, color: FGColor) = polygon(pos = PointN.ZERO, points, color, layout = OF_L)
    fun polygonC(points: List<PointN>, color: FGColor) = polygon(pos = PointN.ZERO, points, color, layout = OF_C)
    fun polygonR(points: List<PointN>, color: FGColor) = polygon(pos = PointN.ZERO, points, color, layout = OF_R)

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(shape: Shape, color: FGColor) = draw(pos = PointN.ZERO, shape, color)
    fun drawL(shape: Shape, color: FGColor) = draw(pos = PointN.ZERO, shape, color, layout = OF_L)
    fun drawC(shape: Shape, color: FGColor) = draw(pos = PointN.ZERO, shape, color, layout = OF_C)
    fun drawR(shape: Shape, color: FGColor) = draw(pos = PointN.ZERO, shape, color, layout = OF_R)

    fun drawL(pos: PointN, shape: Shape, color: FGColor) = draw(pos, shape, color, layout = OF_L)
    fun drawC(pos: PointN, shape: Shape, color: FGColor) = draw(pos, shape, color, layout = OF_C)
    fun drawR(pos: PointN, shape: Shape, color: FGColor) = draw(pos, shape, color, layout = OF_R)

    ////////////////////////////////////////////////////////////////////////////////////////////////////
}
