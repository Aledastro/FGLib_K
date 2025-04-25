package com.uzery.fglib.core.obj

import com.uzery.fglib.utils.data.getter.value.DrawLayerValue
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN

/**
 * @property z [AffineGraphics] drawPOS change value
 * @property sort to compare draw order with another [DrawLayer]
 * @property name for export in [DrawLayerValue]
 * @property suf to compare draw order with another [DrawLayerValue]
 **/
data class DrawLayer(
    val z: PointN,
    private val sort: Double,
    private val name: String = "unnamed",
    private val suf: Int = 0
): Comparable<DrawLayer> {
    companion object {
        val CAMERA_OFF = DrawLayer(PointN(0.0, 0.0), 0.0, "UTIL_CAMERA_OFF")
        val CAMERA_FOLLOW = DrawLayer(PointN(1.0, 1.0), 1.0, "UTIL_CAMERA_FOLLOW")
    }

    operator fun plus(i: Int): DrawLayer {
        return DrawLayer(z, sort, name, suf+i)
    }

    operator fun minus(i: Int): DrawLayer {
        return DrawLayer(z, sort, name, suf-i)
    }

    override fun compareTo(other: DrawLayer): Int {
        val f = this.sort.compareTo(other.sort)
        if (f != 0) return f

        return this.suf.compareTo(other.suf)
    }

    internal val name_with_suf: String
        get() {
            return name+when {
                suf < 0 -> suf
                suf > 0 -> "+$suf"
                else -> ""
            }
        }
}
