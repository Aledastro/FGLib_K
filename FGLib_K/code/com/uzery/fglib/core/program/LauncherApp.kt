package com.uzery.fglib.core.program

import com.uzery.fglib.javafx_rl.JavaFXRealisation
import com.uzery.fglib.javafx_rl.ProgramFX
import javafx.application.Application
import javafx.stage.Stage

internal class LauncherApp: Application() {
    override fun start(stage: Stage) {
        JavaFXRealisation.startWith(stage)
    }
}
