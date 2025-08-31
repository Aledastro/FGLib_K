package com.uzery.fglib.utils.graphics.sub

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.SplitUtils
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class FillGraphics(agc: AffineGraphics): GeometryGraphics(agc) {
    protected abstract fun renderText(pos: PointN, text: String, font: FGFont, color: FGColor)

    var font = FGFont.default_font

    fun setDefaults() {
        font = FGFont.default_font
    }

    ///////////////////////////////////////////////////////////////////////////

    fun text(pos: PointN, text: String, color: FGColor, layout: PointN = OF_L) {
        val size = agc.textSizeOf(text, font)
        renderIn(pos, size, layout) { real_pos, real_size ->
            renderText(
                pos = real_pos,
                text = text,
                font = font.resize(transform.t_size(PointN(font.size)).X),
                color = agc.getAlphaColor(color)
            )
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    fun textL(pos: PointN, text: String, color: FGColor) = text(pos, text, color, layout = OF_TL)
    fun textC(pos: PointN, text: String, color: FGColor) = text(pos, text, color, layout = OF_TC)
    fun textR(pos: PointN, text: String, color: FGColor) = text(pos, text, color, layout = OF_TR)

    ///////////////////////////////////////////////////////////////////////////
}
