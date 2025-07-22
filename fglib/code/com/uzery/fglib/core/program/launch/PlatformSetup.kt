package com.uzery.fglib.core.program.launch

import com.uzery.fglib.core.program.Platform.addUpdatable
import com.uzery.fglib.core.program.Platform.char_keyboard
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.core.program.extension.Extension
import com.uzery.fglib.core.realisation.FGRealisation

/**
 * TODO("doc")
 **/
object PlatformSetup {
    internal lateinit var realisation: FGRealisation

    fun init(realisation: FGRealisation) {
        PlatformSetup.realisation = realisation

        addUpdatable(keyboard, char_keyboard, mouse)
    }

    fun startProcess(options: LaunchOptions, vararg ets: Extension) {
        realisation.startProcess(options, *ets)
    }

    fun startProcess(configuration: LaunchConfiguration) {
        configuration.init()
        startProcess(configuration.options, configuration.main)
    }
}
