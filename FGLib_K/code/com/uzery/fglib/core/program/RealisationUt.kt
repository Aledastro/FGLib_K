package com.uzery.fglib.core.program

import com.uzery.fglib.core.realisation.FGRealisation

object RealisationUt {
    internal lateinit var realisation: FGRealisation

    fun init(realisation: FGRealisation) {
        this.realisation = realisation
    }
}
