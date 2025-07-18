package com.uzery.fglib.core.realisation

import com.uzery.fglib.core.program.FGClipboard
import com.uzery.fglib.core.program.ProgramWindow
import com.uzery.fglib.core.program.data.FGCursor
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class FGProgram {
    abstract var SCREEN_SIZE: PointN

    abstract fun exit()

    abstract fun reRender()

    abstract fun setCursor(cursor: FGCursor)

    abstract val clipboard: FGClipboard

    abstract val window: ProgramWindow
}
