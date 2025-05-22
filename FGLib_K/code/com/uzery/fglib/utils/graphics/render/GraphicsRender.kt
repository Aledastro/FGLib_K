package com.uzery.fglib.utils.graphics.render

import com.uzery.fglib.core.program.extension.FGLayout
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

class GraphicsRender(val pos: PointN, val size: PointN) {
    val main = RectN(pos, size)

    fun layout(k_pos: PointN): PointN {
        return pos+size*k_pos
    }

    fun layout(kx: Double, ky: Double): PointN {
        return pos+size*PointN(kx, ky)
    }

    private fun layout(layout: FGLayout) = layout(layout.value)

    val TOP_LEFT = layout(FGLayout.TOP_LEFT)
    val TOP = layout(FGLayout.TOP)
    val TOP_RIGHT = layout(FGLayout.TOP_RIGHT)
    val LEFT = layout(FGLayout.LEFT)
    val CENTER = layout(FGLayout.CENTER)
    val RIGHT = layout(FGLayout.RIGHT)
    val BOTTOM_LEFT = layout(FGLayout.BOTTOM_LEFT)
    val BOTTOM = layout(FGLayout.BOTTOM)
    val BOTTOM_RIGHT = layout(FGLayout.BOTTOM_RIGHT)
}
