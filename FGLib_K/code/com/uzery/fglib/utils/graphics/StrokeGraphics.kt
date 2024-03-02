package com.uzery.fglib.utils.graphics

abstract class StrokeGraphics: GeometryGraphics() {
    abstract var width: Double
    abstract fun dashes(offset: Double, vararg dashes: Double)
}
