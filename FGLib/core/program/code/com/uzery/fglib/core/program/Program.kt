package com.uzery.fglib.core.program

import com.uzery.fglib.utils.math.geom.PointN
import javafx.animation.AnimationTimer
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.stage.Stage

internal class Program {
    companion object {
        var WIDTH: Double = 0.0
        var HEIGHT: Double = 0.0
        private lateinit var stage: Stage

        lateinit var gc: GraphicsContext

        internal val pressed = Array(KeyCode.values().size) { false }
        internal val mouse_pressed = Array(KeyCode.values().size) { false }
        internal var mouseP = PointN.ZERO

        private var options = LaunchOptions.default

        private lateinit var runnable: RunnableU

        internal fun initWith(runnable: RunnableU, options: LaunchOptions) {
            this.runnable = runnable
            this.options = options
            WIDTH = options.width
            HEIGHT = options.height
        }

        internal fun startWith(stage: Stage) {
            this.stage = stage
            val canvas = Canvas(WIDTH, HEIGHT)
            gc = canvas.graphicsContext2D
            this.stage.scene = Scene(Group(canvas))
            this.stage.show()

            this.stage.scene.setOnMousePressed { e ->
                run {
                    mouseP = PointN(e.x, e.y)
                    mouse_pressed[e.button.ordinal] = true
                }
            }
            this.stage.scene.setOnMouseReleased { e ->
                run {
                    mouseP = PointN(e.x, e.y)
                    mouse_pressed[e.button.ordinal] = false
                }
            }
            this.stage.scene.setOnMouseMoved { e ->
                run {
                    mouseP = PointN(e.x, e.y)
                    mouse_pressed[e.button.ordinal] = false
                }
            }
            this.stage.scene.setOnMouseDragged { e ->
                run {
                    mouseP = PointN(e.x, e.y)
                    mouse_pressed[e.button.ordinal] = true
                }
            }
            this.stage.scene.setOnKeyPressed { key -> pressed[key.code.ordinal] = true }
            this.stage.scene.setOnKeyReleased { key -> pressed[key.code.ordinal] = false }

            runnable.init()

            class ProgramTimer: AnimationTimer() {
                override fun handle(t: Long) {
                    runnable.update()
                }
            }

            val p = ProgramTimer()

            p.start()
        }
    }
}