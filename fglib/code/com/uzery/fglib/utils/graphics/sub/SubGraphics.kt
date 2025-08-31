package com.uzery.fglib.utils.graphics.sub

import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN

abstract class SubGraphics(protected val agc: AffineGraphics) {
    protected val transform
        get() = agc.global_transform

    protected var OF_L = PointN(0.0, 0.0)
    protected var OF_C = PointN(0.5, 0.5)
    protected var OF_R = PointN(1.0, 1.0)

    protected var OF_TL = PointN(0.0, 0.0)
    protected var OF_TC = PointN(0.5, 0.0)
    protected var OF_TR = PointN(1.0, 0.0)

    ///////////////////////////////////////////////////////////////////////////

    var whole_draw = false

    ///////////////////////////////////////////////////////////////////////////

    protected fun renderIn(
        pos: PointN,
        size: PointN,
        layout: PointN,
        f: (PointN, PointN) -> Unit
    ) {
        val layout_pos = pos-size*layout
        val layout_size = size

        if (agc.isOutOfBounds(layout_pos, layout_size)) return

        f(
            transform.pos(layout_pos),
            transform.size(layout_pos, layout_size)
        )
    }
}
