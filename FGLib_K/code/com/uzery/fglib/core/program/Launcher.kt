package com.uzery.fglib.core.program

import javafx.application.Application
import javafx.stage.Stage

object Launcher {
    fun startProcess(options: LaunchOptions, vararg ets: Extension) {
        Program.initWith(options, *ets)
        Application.launch(LauncherApp::class.java)
    }

    fun startProcess(vararg ets: Extension) {
        Program.initWith(LaunchOptions.default, *ets)
        Application.launch(LauncherApp::class.java)
    }
}
