package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN

abstract class FGRealisation<RealisationColor, RealisationFont, RealisationKey, RealisationMouseKey>{
    abstract val graphics: FGGraphics
    abstract val program: FGProgram
    abstract val listener: FGListener
    abstract val packager: FGPackager<RealisationColor, RealisationFont, RealisationKey, RealisationMouseKey>
    abstract fun text_size(text: String, font: FGFont): PointN
}
