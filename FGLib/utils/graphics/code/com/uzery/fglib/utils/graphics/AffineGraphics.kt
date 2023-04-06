package com.uzery.fglib.utils.graphics

abstract class AffineGraphics() {
    val transform = AffineTransform { p -> p }

    abstract val fill: GeometryGraphics
    abstract val stroke: GeometryGraphics
}
