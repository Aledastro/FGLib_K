package com.uzery.fglib.utils.graphics

abstract class AffineGraphics {
    abstract fun setStroke(size: Double)

    abstract val image: ImageGraphics

    abstract val fill: GeometryGraphics
    abstract val stroke: GeometryGraphics
}
