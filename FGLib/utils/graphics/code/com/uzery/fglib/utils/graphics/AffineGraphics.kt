package com.uzery.fglib.utils.graphics

abstract class AffineGraphics {
    abstract val transform: AffineTransform

    abstract val fill: GeometryGraphics
    abstract val stroke: GeometryGraphics
}
