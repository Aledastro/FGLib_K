package com.uzery.fglib.utils.graphics.data

data class FGFont(var family: String, var size: Double, var weight: FGFontWeight, var posture: FGFontPosture) {
    constructor(family: String, size: Double): this(family, size, default_font.weight, default_font.posture)
    constructor(size: Double): this(default_font.family, size)

    fun resize(size: Double): FGFont {
        return FGFont(family, size, weight, posture)
    }

    companion object {
        val default_font = FGFont("arial", 10.0, FGFontWeight.BOLD, FGFontPosture.REGULAR)
    }
}
