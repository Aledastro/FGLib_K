package com.uzery.fglib.utils.graphics.render

import com.uzery.fglib.utils.math.geom.FGLayout
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.max
import kotlin.math.min

class GraphicsRender(val pos: PointN, val size: PointN) {
    val draw_area = RectN(pos, size)

    fun layout(k_pos: PointN): PointN {
        return pos+size*k_pos
    }

    fun child(d_pos: PointN, size: PointN, layout: FGLayout = FGLayout.TOP_LEFT): GraphicsRender {
        return GraphicsRender(pos+d_pos-size*layout.value, size)
    }
    fun childFrom(d_pos1: PointN, d_pos2: PointN): GraphicsRender {
        val min = PointN.transform(d_pos1, d_pos2) { a, b -> min(a, b) }
        val max = PointN.transform(d_pos1, d_pos2) { a, b -> max(a, b) }
        return child(min, max-min)
    }
    fun childFromLayout(k_pos1: PointN, k_pos2: PointN): GraphicsRender {
        return childFrom(layout(k_pos1), layout(k_pos2))
    }

    fun layout(kx: Double, ky: Double) = layout(PointN(kx, ky))

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
