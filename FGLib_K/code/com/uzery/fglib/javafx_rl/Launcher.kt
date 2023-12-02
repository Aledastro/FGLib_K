package com.uzery.fglib.javafx_rl

import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.LaunchOptions
import com.uzery.fglib.core.program.Platform
import javafx.application.Application

object Launcher {
    fun startProcess(options: LaunchOptions, vararg ets: Extension) {
        System.setProperty("quantum.multithreaded", "false")
        Platform.init()
        JavaFXRealisation.initWith(options, *ets)
        Application.launch(LauncherApp::class.java)
    }

    fun startProcess(vararg ets: Extension) {
        startProcess(LaunchOptions.default, *ets)
    }
}
