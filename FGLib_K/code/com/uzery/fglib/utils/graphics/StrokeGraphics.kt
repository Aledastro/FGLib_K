package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN

abstract class StrokeGraphics(agc: AffineGraphics): GeometryGraphics(agc) {
    var width = 1.0
        get() {
            val width = agc.transform.t_size(PointN(field)).X
            setWidth0(width)
            return width
        }

    abstract fun setWidth0(width: Double)

    abstract fun dashes(offset: Double, vararg dashes: Double)

    override fun setDefaults() {
        super.setDefaults()
        width = 1.0
    }
}
