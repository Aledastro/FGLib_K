package com.uzery.fglib.javafx_rl

import com.uzery.fglib.core.program.*
import com.uzery.fglib.core.realisation.FGGraphics
import com.uzery.fglib.core.realisation.FGListener
import com.uzery.fglib.core.realisation.FGProgram
import com.uzery.fglib.core.realisation.FGRealisation
import javafx.animation.AnimationTimer
import javafx.stage.Stage

internal object JavaFXRealisation: FGRealisation() {
    override val graphics: FGGraphics
        get() = GraphicsFX
    override val program: FGProgram
        get() = ProgramFX
    override val listener: FGListener
        get() = ListenerFX

    fun startWith(stage: Stage) {
        ProgramFX.initWith(stage)
        ListenerFX.initListeners(stage)

        Program.init(*ets)
        val timer = object: AnimationTimer() {
            override fun handle(t: Long) {
                Program.loop()
            }
        }
        timer.start()
    }

    fun initWith(options: LaunchOptions, vararg ets: Extension) {
        Platform.options = options
        this.ets = ets
    }

    private lateinit var ets: Array<out Extension>
}
