package com.uzery.fglib.javafx_rl

import com.uzery.fglib.core.realisation.FGPackager
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.input.data.FGKey
import com.uzery.fglib.utils.input.data.FGMouseKey
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight


object PackagerFX: FGPackager<Color, Font, KeyCode, MouseButton>() {
    override fun fromMouseKey(key: String): FGMouseKey {
        return FGMouseKey(key, MouseButton.valueOf(key).ordinal)
    }

    override fun fromKey(key: String): FGKey {
        return FGKey(key, KeyCode.valueOf(key).ordinal)
    }
    override fun fromColor(c: String): FGColor {
        val color = Color.valueOf(c)
        return FGColor(color.red, color.green, color.blue, color.opacity)
    }





    override fun fromFGColor(c: FGColor): Color {
        return Color(c.red, c.green, c.blue, c.alpha)
    }

    override fun fromFGFont(f: FGFont): Font {
        return Font.font(f.family, FontWeight.valueOf(f.weight.name), FontPosture.valueOf(f.posture.name), f.size)
    }

    override fun fromFGKey(key: FGKey): KeyCode {
        return KeyCode.valueOf(key.value)
    }

    override fun fromFGMouseKey(key: FGMouseKey): MouseButton {
        return MouseButton.valueOf(key.value)
    }
}