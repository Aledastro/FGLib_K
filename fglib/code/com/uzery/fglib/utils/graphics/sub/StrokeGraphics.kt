package com.uzery.fglib.utils.graphics.sub

import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class StrokeGraphics(agc: AffineGraphics): GeometryGraphics(agc) {
    protected abstract fun renderLineTo(pos1: PointN, pos2: PointN, color: FGColor)

    protected abstract fun renderPolyline(points: List<PointN>, color: FGColor)

    protected abstract fun utilSetWidth(width: Double)

    abstract fun dashes(offset: Double, vararg dashes: Double)

    var width = 1.0
        set(value) {
            val width = transform.t_size(PointN(value)).X
            utilSetWidth(width)
            field = value
        }

    fun setDefaults() {
        width = 1.0
        dashes(0.0)
    }

    ///////////////////////////////////////////////////////////////////////////

    fun lineTo(pos1: PointN, pos2: PointN, color: FGColor) {
        if (agc.isOutOfBounds(pos1, pos2-pos1)) return

        renderLineTo(transform.pos(pos1), transform.pos(pos2), agc.getAlphaColor(color))
    }

    fun polyline(pos: PointN, points: List<PointN>, color: FGColor, layout: PointN = OF_L) {
        val transformed_points = polyTransform(pos, points, layout) ?: return
        renderPolyline(transformed_points, agc.getAlphaColor(color))
    }

    ///////////////////////////////////////////////////////////////////////////
    fun line(pos: PointN, size: PointN, color: FGColor, layout: PointN = OF_L) {
        val layout_pos = pos-size*layout
        val layout_size = size

        lineTo(layout_pos, layout_pos+layout_size, color)
    }

    fun lineL(pos: PointN, size: PointN, color: FGColor) = line(pos, size, color, layout = OF_L)
    fun lineC(pos: PointN, size: PointN, color: FGColor) = line(pos, size, color, layout = OF_C)
    fun lineR(pos: PointN, size: PointN, color: FGColor) = line(pos, size, color, layout = OF_R)

    ///////////////////////////////////////////////////////////////////////////

    fun polylineL(pos: PointN, points: List<PointN>, color: FGColor) = polyline(pos, points, color, layout = OF_L)
    fun polylineC(pos: PointN, points: List<PointN>, color: FGColor) = polyline(pos, points, color, layout = OF_C)
    fun polylineR(pos: PointN, points: List<PointN>, color: FGColor) = polyline(pos, points, color, layout = OF_R)

    fun polyline(points: List<PointN>, color: FGColor) = polyline(pos = PointN.ZERO, points, color)
    fun polylineL(points: List<PointN>, color: FGColor) = polyline(pos = PointN.ZERO, points, color, layout = OF_L)
    fun polylineC(points: List<PointN>, color: FGColor) = polyline(pos = PointN.ZERO, points, color, layout = OF_C)
    fun polylineR(points: List<PointN>, color: FGColor) = polyline(pos = PointN.ZERO, points, color, layout = OF_R)
}
