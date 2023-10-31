package com.uzery.fglib.core.program

import javafx.application.Application
import javafx.stage.Stage

class LauncherOld: Application() {
    fun startProcess(options: LaunchOptions, vararg ets: Extension) {
        System.setProperty("quantum.multithreaded", "false")
        Program.initWith(options, *ets)
        launch()
    }

    fun startProcess(vararg ets: Extension) {
        startProcess(LaunchOptions.default, *ets)
    }

    override fun start(stage: Stage) {
        Program.startWith(stage)
    }
}
