package com.uzery.fglib.utils.graphics

abstract class StrokeGraphics(agc: AffineGraphics): GeometryGraphics(agc) {
    abstract var width: Double
    abstract fun dashes(offset: Double, vararg dashes: Double)
}
