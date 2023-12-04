package com.uzery.fglib.utils.graphics.data

import com.uzery.fglib.core.program.Platform.packager

data class FGColor(val red: Double, val green: Double, val blue: Double, val alpha: Double = 1.0) {
    fun changeAlpha(a: Double): FGColor {
        return FGColor(red, green, blue, a)
    }

    fun transparent(k: Double): FGColor {
        return changeAlpha(alpha*k.coerceIn(0.0, 1.0))
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
        val WHITE = from("WHITE")
        val BLACK = from("BLACK")
        val TRANSPARENT = from("TRANSPARENT")

        val RED = from("RED")
        val ORANGE = from("ORANGE")
        val ORANGERED = from("ORANGERED")
        val BLUE = from("BLUE")
        val GREEN = from("GREEN")
        val BEIGE = from("BEIGE")
        val GOLD = from("GOLD")
        val CYAN = from("CYAN")
        val GRAY = from("GRAY")
        val DARKGRAY = from("DARKGRAY")
        val DARKBLUE = from("DARKBLUE")
        val LIGHTGRAY = from("LIGHTGRAY")
        val PURPLE = from("PURPLE")
        val DEEPSKYBLUE = from("DEEPSKYBLUE")

        fun from(c: String): FGColor {
            return packager.fromColor(c)
        }


        fun web(web: String): FGColor {
            return from(web) //todo
        }

        fun gray(c: Double, a: Double = 1.0): FGColor {
            return FGColor(c, c, c, a)
        }
    }
}
