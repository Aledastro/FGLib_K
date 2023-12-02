package com.uzery.fglib.utils.graphics.data

import javafx.scene.paint.Color

data class FGColor(val red: Double, val green: Double, val blue: Double, val alpha: Double = 1.0) {
    fun changeAlpha(a: Double): FGColor {
        return FGColor(red, green, blue, a)
    }

    fun transparent(k: Double): FGColor {
        val real_k = k.coerceIn(0.0, 1.0)
        return changeAlpha(alpha*real_k)
    }

    fun interpolate(other: FGColor, k: Double): FGColor {
        val real_k = k.coerceIn(0.0, 1.0)
        return FGColor(
            this.red+(other.red-this.red)*real_k,
            this.green+(other.green-this.green)*real_k,
            this.blue+(other.blue-this.blue)*real_k,
            this.alpha+(other.alpha-this.alpha)*real_k
        )
    }

    companion object {
        val WHITE = from(Color.WHITE)
        val BLACK = from(Color.BLACK)
        val TRANSPARENT = from(Color.TRANSPARENT)

        val RED = from(Color.RED)
        val ORANGE = from(Color.ORANGE)
        val ORANGERED = from(Color.ORANGERED)
        val BLUE = from(Color.BLUE)
        val GREEN = from(Color.GREEN)
        val BEIGE = from(Color.BEIGE)
        val GOLD = from(Color.GOLD)
        val CYAN = from(Color.CYAN)
        val GRAY = from(Color.GRAY)
        val DARKGRAY = from(Color.DARKGRAY)
        val DARKBLUE = from(Color.DARKBLUE)
        val LIGHTGRAY = from(Color.LIGHTGRAY)
        val PURPLE = from(Color.PURPLE)
        val DEEPSKYBLUE = from(Color.DEEPSKYBLUE)


        fun web(web: String): FGColor {
            return from(Color.web(web)) //todo
        }

        fun gray(c: Double, a: Double = 1.0): FGColor {
            return FGColor(c, c, c, a)
        }

        fun from(c: Color): FGColor {
            return FGColor(c.red, c.green, c.blue, c.opacity)
        }

        fun fromFGColor(c: FGColor): Color {
            return Color.color(c.red, c.green, c.blue, c.alpha)
        }
    }
}
