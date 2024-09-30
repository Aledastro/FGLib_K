package com.uzery.fglib.utils.graphics

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.TextUtils
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.graphics.data.FGFontPosture
import com.uzery.fglib.utils.graphics.data.FGFontWeight
import com.uzery.fglib.utils.math.geom.PointN

abstract class FillGraphics(agc: AffineGraphics): GeometryGraphics(agc) {
    protected abstract fun text0(pos: PointN, text: String, font: FGFont, color: FGColor)

    var font = FGFont.default_font

    override fun setDefaults() {
        super.setDefaults()
        font = FGFont.default_font
    }

    fun setFullDefaults() {
        setDefaults()
    }

    fun font(
        family: String = font.family,
        size: Double = font.size,
        weight: FGFontWeight = font.weight,
        posture: FGFontPosture = font.posture
    ) {
        font = FGFont(family, size, weight, posture)
    }

    fun font(size: Double = font.size, weight: FGFontWeight = font.weight, posture: FGFontPosture = font.posture) {
        font = FGFont(font.family, size, weight, posture)
    }

    fun text_size(text: String, font: FGFont = this.font): PointN {
        return graphics.text_size(text, font)
    }

    fun splitText(text: String, width: Double, font: FGFont = this.font): ArrayList<String> {
        return TextUtils.splitText(text, width) { stroke -> text_size(stroke, font).X }
    }

    fun splitTextAndMerge(text: String, width: Double, font: FGFont = this.font): String {
        return TextUtils.splitTextAndMerge(text, width) { stroke -> text_size(stroke, font).X }
    }

    ///////////////////////////////////////////////////////////////////////////

    fun text(pos: PointN, text: String, font: FGFont, color: FGColor) {
        if (agc.isOutOfBounds(pos, text_size(text, font))) return

        agc.applyAlphaWith(alpha)
        text0(transform.pos(pos), text, font.resize(transform.t_size(PointN(font.size)).X), color)
    }

    fun textL(pos: PointN, text: String, font: FGFont, color: FGColor) = text(pos, text, font, color)
    fun textC(pos: PointN, text: String, font: FGFont, color: FGColor) =
        text(pos-text_size(text, font).XP/2, text, font, color)

    fun textR(pos: PointN, text: String, font: FGFont, color: FGColor) =
        text(pos-text_size(text, font).XP, text, font, color)

    ///////////////////////////////////////////////////////////////////////////

    fun text(pos: PointN, text: String, color: FGColor) = text(pos, text, font, color)
    fun textL(pos: PointN, text: String, color: FGColor) = textL(pos, text, font, color)
    fun textC(pos: PointN, text: String, color: FGColor) = textC(pos, text, font, color)
    fun textR(pos: PointN, text: String, color: FGColor) = textR(pos, text, font, color)

    ///////////////////////////////////////////////////////////////////////////
}
