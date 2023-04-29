package com.uzery.fglib.core.program

import com.uzery.fglib.utils.math.geom.PointN
import javafx.animation.AnimationTimer
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import java.util.*

internal class Program {
    companion object {
        private lateinit var stage: Stage

        internal lateinit var gc: GraphicsContext

        internal val pressed = Array(KeyCode.values().size) { false }
        internal val mouse_pressed = Array(KeyCode.values().size) { false }
        internal var mouseP = PointN.ZERO
        internal var options = LaunchOptions.default

        private var extensions = LinkedList<Extension>()

        internal fun initWith(options: LaunchOptions, vararg ets: Extension) {
            val list = LinkedList<Extension>()
            list.addAll(ets)
            while(list.isNotEmpty()) {
                val e = list.removeFirst()
                extensions.add(e)
                list.addAll(e.children())
            }

            this.options = options
        }

        internal fun startWith(stage: Stage) {
            this.stage = stage
            val canvas = Canvas(options.size.X, options.size.Y)
            gc = canvas.graphicsContext2D
            stage.scene = Scene(Group(canvas))
            stage.initStyle(options.style)
            stage.show()

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

            extensions.forEach { e -> e.init() }

            class ProgramTimer: AnimationTimer() {
                override fun handle(t: Long) {
                    extensions.forEach { e -> e.update() }
                }
            }

            val p = ProgramTimer()
            p.start()
        }
    }
}