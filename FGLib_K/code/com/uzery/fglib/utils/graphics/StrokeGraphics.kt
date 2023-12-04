package com.uzery.fglib.utils.graphics

abstract class StrokeGraphics(transform: AffineTransform, transformSize: AffineTransform): GeometryGraphics(transform, transformSize) {
    var width = 1.0

    abstract var lineDashOffset: Double
    abstract fun setLineDashes(vararg dashes: Double)
}
