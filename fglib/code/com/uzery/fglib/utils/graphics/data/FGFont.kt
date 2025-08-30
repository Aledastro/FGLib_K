package com.uzery.fglib.utils.graphics.data

/**
 * TODO("doc")
 **/
data class FGFont(
    val family: String,
    val size: Double,
    val weight: FGFontWeight = default_font.weight,
    val posture: FGFontPosture = default_font.posture
) {
    constructor(
        size: Double,
        weight: FGFontWeight = default_font.weight,
        posture: FGFontPosture = default_font.posture
    ): this(
        default_font.family, size, weight, posture
    )

    fun resize(size: Double): FGFont {
        return FGFont(family, size, weight, posture)
    }

    companion object {
        val default_font = FGFont("arial", 10.0, FGFontWeight.STANDARD, FGFontPosture.REGULAR)
    }
}
