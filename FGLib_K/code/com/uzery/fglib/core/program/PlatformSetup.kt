package com.uzery.fglib.core.program

import com.uzery.fglib.core.realisation.FGRealisation

object PlatformSetup {
    internal lateinit var realisation: FGRealisation

    fun init(realisation: FGRealisation) {
        this.realisation = realisation
    }

    fun startProcess(options: LaunchOptions, vararg ets: Extension) {
        realisation.startProcess(options, *ets)
    }

    fun startProcess(vararg ets: Extension) {
        startProcess(LaunchOptions.default, *ets)
    }

    fun startProcess(configuration: LaunchConfiguration) {
        configuration.init()
        startProcess(configuration.options, configuration.main)
    }
}
