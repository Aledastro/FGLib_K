package com.uzery.fglib.core.realisation

import com.uzery.fglib.core.program.FGClipboard
import com.uzery.fglib.core.program.data.FGCursor
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class FGProgram {
    abstract var SCREEN_SIZE: PointN
    abstract var WINDOW_SIZE: PointN

    abstract fun iconify(iconify: Boolean)
    abstract fun maximize(maximize: Boolean)
    abstract fun exit()
    abstract fun setCursor(cursor: FGCursor)

    abstract val clipboard: FGClipboard

}
