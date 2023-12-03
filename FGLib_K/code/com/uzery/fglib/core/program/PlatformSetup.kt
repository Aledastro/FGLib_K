package com.uzery.fglib.core.program

import com.uzery.fglib.core.realisation.FGRealisation

object PlatformSetup {
    internal lateinit var realisation: FGRealisation

    fun init(realisation: FGRealisation) {
        this.realisation = realisation
    }
}
