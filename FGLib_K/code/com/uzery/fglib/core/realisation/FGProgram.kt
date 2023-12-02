package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.program.FGCursor

abstract class FGProgram {
    abstract var WINDOW_SIZE: PointN

    abstract fun exit()
    abstract fun setCursor(cursor: FGCursor)

}
