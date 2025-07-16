package com.uzery.fglib.utils.graphics.sub

import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class StrokeGraphics(agc: AffineGraphics): GeometryGraphics(agc) {
    protected abstract fun lineTo0(pos1: PointN, pos2: PointN, color: FGColor)
    protected abstract fun setWidth0(width: Double)

    abstract fun dashes(offset: Double, vararg dashes: Double)

    var width = 1.0
        set(value) {
            val width = transform.t_size(PointN(value)).X
            setWidth0(width)
            field = value
        }

    fun setDefaults() {
        width = 1.0
        dashes(0.0)
    }

    ///////////////////////////////////////////////////////////////////////////

    fun lineTo(pos1: PointN, pos2: PointN, color: FGColor) {
        if (agc.isOutOfBounds(pos1, pos2-pos1)) return

        lineTo0(transform.pos(pos1), transform.pos(pos2), agc.getAlphaColor(color))
    }

    fun line(pos: PointN, size: PointN, color: FGColor) = lineTo(pos, pos+size, color)
    fun lineL(pos: PointN, size: PointN, color: FGColor) = lineTo(pos, pos+size, color)
    fun lineC(pos: PointN, size: PointN, color: FGColor) = lineTo(pos-size/2, pos+size/2, color)
    fun lineR(pos: PointN, size: PointN, color: FGColor) = lineTo(pos-size, pos, color)
}
