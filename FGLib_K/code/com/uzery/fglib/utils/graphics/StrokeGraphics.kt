package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

abstract class StrokeGraphics(agc: AffineGraphics): GeometryGraphics(agc) {
    protected abstract fun lineTo0(pos1: PointN, pos2: PointN, color: FGColor)
    protected abstract fun setWidth0(width: Double)

    abstract fun dashes(offset: Double, vararg dashes: Double)


    var width = 1.0
        get() {
            val width = agc.transform.t_size(PointN(field)).X
            setWidth0(width)
            return width
        }

    override fun setDefaults() {
        super.setDefaults()
        width = 1.0
    }

    ///////////////////////////////////////////////////////////////////////////

    fun lineTo(pos1: PointN, pos2: PointN, color: FGColor) {
        lineTo0(transform.pos(pos1), transform.pos(pos2), color)
    }

    fun line(pos: PointN, size: PointN, color: FGColor) = lineTo(pos, pos+size, color)
}
