package com.uzery.fglib.core.realisation

import com.uzery.fglib.core.program.FGCursor
import com.uzery.fglib.utils.math.geom.PointN

abstract class FGProgram {
    abstract var WINDOW_SIZE: PointN

    abstract fun exit()
    abstract fun setCursor(cursor: FGCursor)

}
