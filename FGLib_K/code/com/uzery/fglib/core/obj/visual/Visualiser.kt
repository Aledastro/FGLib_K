package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.ObjectComponent
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

interface Visualiser: ObjectComponent {
    fun draw(draw_pos: PointN)

    fun drawWithDefaults(draw_pos: PointN) {
        //if(!ShapeUtils.into(main.copy(draw_pos), Platform.CANVAS_R)) return

        agc.setDefaults()
        agc.layer = drawLayer()
        draw(draw_pos)
    }

    val sortPOS: PointN?
        get() = null

    val main: RectN
    val agc: AffineGraphics
        get() = Platform.graphics

    fun drawLayer(): DrawLayer
}
