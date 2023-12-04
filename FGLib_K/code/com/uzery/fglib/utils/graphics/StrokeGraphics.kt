package com.uzery.fglib.utils.graphics

abstract class StrokeGraphics(transform: AffineTransform, transformSize: AffineTransform):
    GeometryGraphics(transform, transformSize) {
    abstract var width: Double
    abstract fun dashes(offset: Double, vararg dashes: Double)
}
