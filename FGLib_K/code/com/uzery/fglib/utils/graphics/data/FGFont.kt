package com.uzery.fglib.utils.graphics.data

import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight

data class FGFont(val family: String, val size: Double, val weight: FGFontWeight, val posture: FGFontPosture) {
    companion object {
        fun fromFGFont(font: FGFont): Font {
            return Font.font(font.family, FontWeight.valueOf(font.weight.name),
                FontPosture.valueOf(font.posture.name), font.size)
        }
    }
}
