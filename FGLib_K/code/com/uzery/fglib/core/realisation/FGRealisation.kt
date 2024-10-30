package com.uzery.fglib.core.realisation

import com.uzery.fglib.core.program.extension.Extension
import com.uzery.fglib.core.program.launch.LaunchOptions
import com.uzery.fglib.core.realisation.packager.FGPackager
import com.uzery.fglib.utils.graphics.AffineGraphics

abstract class FGRealisation {
    fun update() {
        listener.update()
    }

    abstract val graphics: AffineGraphics
    abstract val program: FGProgram
    abstract val listener: FGListener
    abstract val packager: FGPackager

    abstract fun startProcess(options: LaunchOptions, vararg ets: Extension)
}
