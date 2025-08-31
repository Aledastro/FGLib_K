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

    fun line(pos: PointN, size: PointN, color: FGColor, layout: PointN = OF_L) {
        renderIn(pos, size, layout) { real_pos, real_size ->
            renderLineTo(real_pos, real_pos+real_size, agc.getAlphaColor(color))
        }
    }

    fun lineTo(pos1: PointN, pos2: PointN, color: FGColor, layout: PointN = OF_L) {
        line(pos1, pos2-pos1, color, layout)
    }

    fun polyline(pos: PointN, points: List<PointN>, color: FGColor, layout: PointN = OF_L) {
        val (p_pos, p_size) = getPolyBounds(pos, points) ?: return
        val dp = pos-p_pos
        renderIn(p_pos, p_size, layout) { real_pos, real_size ->
            val transformed_points = points.map { p -> transform.pos(p+dp)+real_pos }
            renderPolyline(transformed_points, agc.getAlphaColor(color))
        }
    }

    ///////////////////////////////////////////////////////////////////////////

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
