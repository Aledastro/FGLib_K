package com.uzery.fglib.core.realisation

import com.uzery.fglib.core.program.FGClipboard
import com.uzery.fglib.core.program.data.FGCursor
import com.uzery.fglib.utils.math.geom.PointN

abstract class FGProgram {
    abstract var WINDOW_SIZE: PointN

    abstract fun exit()
    abstract fun setCursor(cursor: FGCursor)

    abstract val clipboard: FGClipboard

}
