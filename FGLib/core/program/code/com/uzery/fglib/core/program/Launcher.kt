package com.uzery.fglib.core.program

import javafx.application.Application
import javafx.stage.Stage

class Launcher: Application() {
    fun initWith(runnable: RunnableU, options: LaunchOptions) {
        Program.initWith(runnable, options)
    }

    fun initWith(runnable: RunnableU) {
        Program.initWith(runnable, LaunchOptions.default)
    }

    fun startProcess() {
        launch()
    }

    fun startProcess(runnable: RunnableU, options: LaunchOptions) {
        initWith(runnable, options)
        startProcess()
    }

    fun startProcess(runnable: RunnableU) {
        initWith(runnable)
        startProcess()
    }

    override fun start(stage: Stage) {
        Program.startWith(stage)
    }
}
