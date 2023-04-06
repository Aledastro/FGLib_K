package com.uzery.fglib.core.program

import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.GeometryGraphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.animation.AnimationTimer
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.stage.Stage

internal class Program {
    companion object {
        private lateinit var stage: Stage

        lateinit var gc: GraphicsContext

        private var options = LaunchOptions.default

        private lateinit var runnable: RunnableU

        internal fun initWith(runnable: RunnableU, options: LaunchOptions) {
            this.runnable = runnable
            this.options = options
        }

        internal fun startWith(stage: Stage) {
            this.stage = stage
            val canvas = Canvas(options.width, options.height)
            gc = canvas.graphicsContext2D
            this.stage.scene = Scene(Group(canvas))
            this.stage.show()

            class ProgramTimer: AnimationTimer() {
                override fun handle(t: Long) {
                    runnable.update()
                }
            }

            val p = ProgramTimer()

            p.start()
        }
    }

    object AG: AffineGraphics() {
        override val fill: GeometryGraphics = object: GeometryGraphics() {
            override var color: Paint = Color(0.0, 0.0, 0.0, 1.0)
                set(value) {
                    field = value
                    gc.fill = value
                }

            override fun rect(pos: PointN, size: PointN) {
                gc.fillRect(pos.X(), pos.Y(), size.X(), size.Y())
            }

            override fun oval(pos: PointN, size: PointN) {
                gc.fillOval(pos.X(), pos.Y(), size.X(), size.Y())
            }
        }
        override val stroke: GeometryGraphics = object: GeometryGraphics() {
            override var color: Paint = Color(0.0, 0.0, 0.0, 1.0)
                set(value) {
                    field = value
                    gc.stroke = value
                }

            override fun rect(pos: PointN, size: PointN) {
                gc.strokeRect(pos.X(), pos.Y(), size.X(), size.Y())
            }

            override fun oval(pos: PointN, size: PointN) {
                gc.strokeOval(pos.X(), pos.Y(), size.X(), size.Y())
            }
        }
    }
}