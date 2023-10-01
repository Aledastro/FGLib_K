package com.uzery.fglib.core.program

import javafx.application.Application
import javafx.stage.Stage

internal class LauncherApp: Application() {
    override fun start(stage: Stage) {
        Program.startWith(stage)
    }
}
