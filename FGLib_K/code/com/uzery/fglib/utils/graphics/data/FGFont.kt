package com.uzery.fglib.utils.graphics.data

import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight

data class FGFont(val family: String, val size: Double, val weight: FGFontWeight, val posture: FGFontPosture) {
    fun resize(size: Double): FGFont {
        return FGFont(family, size, weight, posture)
    }
}
