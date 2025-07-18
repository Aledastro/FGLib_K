package com.uzery.fglib.core.program

import com.uzery.fglib.utils.math.geom.PointN

abstract class ProgramWindow {
    abstract var pos: PointN
    abstract var size: PointN

    abstract var iconified: Boolean
    abstract var maximized: Boolean
}
