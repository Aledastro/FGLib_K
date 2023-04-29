package com.uzery.fglib.core.program

import javafx.application.Application
import javafx.stage.Stage

class Launcher: Application() {
    fun startProcess(options: LaunchOptions, vararg ets: Extension) {
        Program.initWith(options, *ets)
        launch()
    }

    fun startProcess(vararg ets: Extension) {
        Program.initWith(LaunchOptions.default, *ets)
        launch()
    }

    override fun start(stage: Stage) {
        Program.startWith(stage)
    }
}
