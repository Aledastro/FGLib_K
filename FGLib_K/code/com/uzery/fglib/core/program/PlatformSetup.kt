package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Platform.addUpdatable
import com.uzery.fglib.core.program.Platform.char_keyboard
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.core.realisation.FGRealisation

object PlatformSetup {
    internal lateinit var realisation: FGRealisation

    fun init(realisation: FGRealisation) {
        this.realisation = realisation

        addUpdatable(keyboard, char_keyboard, mouse.keys)
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
