package com.uzery.fglib.utils.graphics.data

import com.uzery.fglib.utils.data.debug.DebugData

/**
 * TODO("doc")
 **/
data class FGColor(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255) {
    val argb: Int
        get() = (alpha shl 24)+(red shl 16)+(green shl 8)+(blue shl 0)

    constructor(red: Int, green: Int, blue: Int, alpha: Double = 1.0): this(
        red, green, blue, (alpha*255).toInt()
    )

    constructor(red: Double, green: Double, blue: Double, alpha: Double = 1.0): this(
        (red*255).toInt(), (green*255).toInt(), (blue*255).toInt(), (alpha*255).toInt()
    )

    fun changeAlpha(a: Double): FGColor {
        return FGColor(red, green, blue, a)
    }

    fun transparent(k: Double): FGColor {
        return changeAlpha(alpha*k.coerceIn(0.0, 1.0)/255.0)
    }

    fun interpolate(other: FGColor, k: Double): FGColor {
        val real_k = k.coerceIn(0.0, 1.0)
        return FGColor(
            (this.red+(other.red-this.red)*real_k).toInt(),
            (this.green+(other.green-this.green)*real_k).toInt(),
            (this.blue+(other.blue-this.blue)*real_k).toInt(),
            (this.alpha+(other.alpha-this.alpha)*real_k).toInt()
        )
    }

    companion object {
        ////////////////////////////////////////////////////////////////////////////////////////////

        val TRANSPARENT = web("0x00000000")

        val PURE_BLACK = web("0x000000")
        val PURE_RED = web("0xff0000")
        val PURE_GREEN = web("0x00ff00")
        val PURE_BLUE = web("0x0000ff")
        val PURE_YELLOW = web("0xffff00")
        val PURE_PURPLE = web("0xff00ff")
        val PURE_CYAN = web("0x00ffff")
        val PURE_WHITE = web("0xffffff")

        ////////////////////////////////////////////////////////////////////////////////////////////

        val BLACK = web("0x161616")
        val RED = web("0xb91b42")
        val GREEN = web("0x269e23")
        val BLUE = web("0x295cd3")
        val YELLOW = web("0xe0dd19")
        val PURPLE = web("0xb00ea6")
        val CYAN = web("0x58c5ca")
        val WHITE = web("0xe0e0e0")

        val BEIGE = web("0xcac9b3")
        val GOLD = web("0xa39103")
        val ORANGE = web("0xc97e1e")
        val ORANGE_RED = web("0xe04b28")
        val DARK_BLUE = web("0x23216e")
        val DEEP_SKY_BLUE = web("0x456ad1")
        val BROWN = web("0x4b3019")

        val LIGHT_GRAY = web("0xb0b0b0")
        val GRAY = web("0x848484")
        val DARK_GRAY = web("0x393939")

        ////////////////////////////////////////////////////////////////////////////////////////////

        fun web(web: String): FGColor {
            var source = web
            if (web.startsWith("#")) source = web.replace("#", "")
            if (web.startsWith("0x")) source = web.replace("0x", "")

            val sr = "0x"+source.substring(0, 2)
            val sg = "0x"+source.substring(2, 4)
            val sb = "0x"+source.substring(4, 6)
            val sa = "0x"+if (source.length == 8) source.substring(6, 8) else "ff"

            val r = Integer.decode(sr)
            val g = Integer.decode(sg)
            val b = Integer.decode(sb)
            val a = Integer.decode(sa)

            return FGColor(r, g, b, a)
        }

        fun gray(c: Double, a: Double = 1.0): FGColor {
            return FGColor(c, c, c, a)
        }

        fun argb(argb: Int): FGColor {
            val a = (argb shr 24).mod(256)
            val r = (argb shr 16).mod(256)
            val g = (argb shr 8).mod(256)
            val b = (argb shr 0).mod(256)

            return FGColor(r, g, b, a)
        }
    }
}
