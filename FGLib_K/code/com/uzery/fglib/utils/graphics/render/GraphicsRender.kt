package com.uzery.fglib.utils.graphics.render

import com.uzery.fglib.core.program.extension.FGLayout
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

class GraphicsRender(val pos: PointN, val size: PointN) {
    val main = RectN(pos, size)

    private fun of(layout: FGLayout): PointN {
        return pos+size*layout.value
    }

    val top_left = of(FGLayout.TOP_LEFT)
    val top = of(FGLayout.TOP)
    val top_right = of(FGLayout.TOP_RIGHT)
    val left = of(FGLayout.LEFT)
    val center = of(FGLayout.CENTER)
    val right = of(FGLayout.RIGHT)
    val bottom_left = of(FGLayout.BOTTOM_LEFT)
    val bottom = of(FGLayout.BOTTOM)
    val bottom_right = of(FGLayout.BOTTOM_RIGHT)
}
