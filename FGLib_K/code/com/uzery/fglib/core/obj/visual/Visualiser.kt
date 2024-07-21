package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.ObjectComponent
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

abstract class Visualiser: ObjectComponent {
    abstract fun draw(draw_pos: PointN)
    abstract fun drawLayer(): DrawLayer

    fun drawWithDefaults(draw_pos: PointN) {
        //todo if (main != null && !ShapeUtils.into(main!!.copy(draw_pos), Platform.CANVAS_R)) return

        agc.setDefaults()
        agc.layer = drawLayer()
        draw(draw_pos)
    }

    var sortPOS = PointN.ZERO
    protected var main: RectN? = null

    val agc: AffineGraphics
        get() = Platform.graphics
}
