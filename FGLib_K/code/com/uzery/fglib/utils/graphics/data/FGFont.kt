package com.uzery.fglib.utils.graphics.data

data class FGFont(var family: String, var size: Double, var weight: FGFontWeight, var posture: FGFontPosture) {
    fun resize(size: Double): FGFont {
        return FGFont(family, size, weight, posture)
    }
}
