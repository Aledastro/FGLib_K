package com.uzery.fglib.utils.graphics.render

import com.uzery.fglib.core.program.extension.FGLayout
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

class GraphicsRender(val pos: PointN, val size: PointN) {
    val main = RectN(pos, size)

    private fun of(layout: FGLayout): PointN {
        return pos+size*layout.value
    }

    val TOP_LEFT = of(FGLayout.TOP_LEFT)
    val TOP = of(FGLayout.TOP)
    val TOP_RIGHT = of(FGLayout.TOP_RIGHT)
    val LEFT = of(FGLayout.LEFT)
    val CENTER = of(FGLayout.CENTER)
    val RIGHT = of(FGLayout.RIGHT)
    val BOTTOM_LEFT = of(FGLayout.BOTTOM_LEFT)
    val BOTTOM = of(FGLayout.BOTTOM)
    val BOTTOM_RIGHT = of(FGLayout.BOTTOM_RIGHT)
}
