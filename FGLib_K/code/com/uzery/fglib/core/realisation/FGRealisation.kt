package com.uzery.fglib.core.realisation

import com.uzery.fglib.utils.graphics.AffineGraphics

abstract class FGRealisation {
    abstract val graphics: AffineGraphics
    abstract val program: FGProgram
    abstract val listener: FGListener
    abstract val packager: FGPackager
}
