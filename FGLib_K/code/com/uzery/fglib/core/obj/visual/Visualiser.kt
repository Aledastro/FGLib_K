package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN

interface Visualiser {
    fun draw(draw_pos: PointN)

    fun drawWithDefaults(draw_pos: PointN) {
        agc.alpha = 1.0
        agc.layer = drawLayer()
        draw(draw_pos)
    }

    val agc: AffineGraphics
        get() = Platform.graphics

    fun drawLayer(): DrawLayer
}
