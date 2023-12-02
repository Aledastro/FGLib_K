package com.uzery.fglib.javafx_rl

import com.uzery.fglib.core.program.*
import com.uzery.fglib.core.realisation.*
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN
import javafx.animation.AnimationTimer
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontPosture
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Stage

internal object JavaFXRealisation: FGRealisation<Color, Font, KeyCode, MouseButton>() {
    override val graphics: FGGraphics
        get() = GraphicsFX
    override val program: FGProgram
        get() = ProgramFX
    override val listener: FGListener
        get() = ListenerFX
    override val packager: FGPackager<Color, Font, KeyCode, MouseButton>
        get() = PackagerFX

    override fun text_size(text: String, font: FGFont): PointN {
        val t = Text(text)
        t.font = PackagerFX.fromFGFont(font.resize(font.size))
        return PointN(t.layoutBounds.width, t.layoutBounds.height)
    }

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
