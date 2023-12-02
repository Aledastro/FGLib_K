package com.uzery.fglib.javafx_rl


import javafx.application.Application
import javafx.stage.Stage

internal class LauncherApp: Application() {
    override fun start(stage: Stage) {
        JavaFXRealisation.startWith(stage)
    }
}
