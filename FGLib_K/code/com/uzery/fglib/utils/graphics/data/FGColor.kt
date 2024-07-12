package com.uzery.fglib.utils.graphics.data

import com.uzery.fglib.utils.data.debug.DebugData

data class FGColor(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255) {
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
        val WHITE = web("0xffffff")
        val BLACK = web("0x000000")
        val TRANSPARENT = WHITE.transparent(0.0)

        val RED = web("0xff0000")
        val ORANGE = web("0xff7f00")
        val ORANGERED = web("0xff3f00")
        val BLUE = web("0x0000ff")
        val GREEN = web("0x00ff00")
        val BEIGE = web("0xf0f0f0")
        val GOLD = web("0xaf5f00")
        val CYAN = web("0x0000af")
        val GRAY = web("0x5f5f5f")
        val DARKGRAY = web("0x7f7f7f")
        val DARKBLUE = web("0x2f2fff")
        val LIGHTGRAY = web("0x1f1f1f")
        val PURPLE = web("0x5f00ff")
        val DEEPSKYBLUE = web("0x1f3fff")


        fun web(web: String): FGColor {
            var source = web
            if (web.startsWith("#")) source = web.replace("#", "0x")
            if (!source.startsWith("0x")) throw DebugData.error("web color: $web")

            val sr = "0x"+source.substring(2, 4)
            val sg = "0x"+source.substring(4, 6)
            val sb = "0x"+source.substring(6, 8)
            val sa = "0x"+if (source.length == 10) source.substring(8, 10) else "ff"

            val r = Integer.decode(sr)
            val g = Integer.decode(sg)
            val b = Integer.decode(sb)
            val a = Integer.decode(sa)

            return FGColor(r, g, b, a)
        }

        fun gray(c: Double, a: Double = 1.0): FGColor {
            return FGColor(c, c, c, a)
        }
    }
}
