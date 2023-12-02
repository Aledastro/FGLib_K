package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.graphics.AffineGraphics

abstract class FGGraphics {
    abstract var scale: Int
    abstract var lineWidth: Double
    abstract var lineDashOffset: Double
    abstract fun setLineDashes(vararg dashes: Double)

    abstract val graphics: AffineGraphics
    abstract var global_view_scale: Double
    abstract var whole_draw: Boolean
}
