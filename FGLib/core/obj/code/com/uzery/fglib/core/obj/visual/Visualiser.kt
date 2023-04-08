package com.uzery.fglib.core.obj.visual

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN

abstract class Visualiser {
    abstract fun draw(pos: PointN)

    fun agc(): AffineGraphics = Platform.graphics
    abstract fun drawLayer(): DrawLayer
}
