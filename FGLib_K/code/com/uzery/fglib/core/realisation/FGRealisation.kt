package com.uzery.fglib.core.realisation

import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.LaunchOptions
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN

abstract class FGRealisation {
    abstract val graphics: AffineGraphics
    abstract val program: FGProgram
    abstract val listener: FGListener
    abstract val packager: FGPackager
}
