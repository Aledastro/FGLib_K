package com.uzery.fglib.core.component.visual

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * One of basic [ObjectComponent]
 *
 * Draws on screen
 *
 * @property agc [AffineGraphics]
 **/
abstract class Visualiser: ObjectComponent, Comparable<Visualiser> {
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

    override fun compareTo(other: Visualiser): Int {
        return this.drawLayer().compareTo(other.drawLayer())
    }
}
