package com.uzery.fglib.core.program

import com.uzery.fglib.javafx_rl.JavaFXRealisation
import com.uzery.fglib.javafx_rl.ProgramFX
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
