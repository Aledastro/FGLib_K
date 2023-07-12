package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Program.Companion.gc
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.AffineTransform
import com.uzery.fglib.utils.graphics.GeometryGraphics
import com.uzery.fglib.utils.graphics.ImageGraphics
import com.uzery.fglib.utils.input.KeyActivator
import com.uzery.fglib.utils.input.TouchActivator
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

class Platform {
    companion object {
        fun options() = Program.options

        fun update() {
            keyboard.update()
            mouse_keys.update()
        }

        var develop_mode = false

        var whole_draw = false

        //todo scale 3 spaces
        var scale = 2

        val WINDOW
            get() = PointN(options().size)
        val CANVAS
            get() = WINDOW/scale

        var global_alpha = 1.0
            set(input) {
                gc.globalAlpha = input*graphics.alpha
                field = input
            }

        val CANVAS_R
            get() = RectN(PointN.ZERO, CANVAS)
        val WINDOW_R
            get() = RectN(PointN.ZERO, WINDOW)

        val keyboard = object: KeyActivator<KeyCode>(KeyCode.values().size) {
            override fun pressed0(code: Int): Boolean = Program.pressed[code]
            override fun from(key: KeyCode): Int = key.ordinal
        }
        val mouse_keys = object: KeyActivator<MouseButton>(KeyCode.values().size) {
            override fun pressed0(code: Int): Boolean = Program.mouse_pressed[code]
            override fun from(key: MouseButton): Int = key.ordinal
        }
        val mouse = object: TouchActivator(RectN(PointN.ZERO, CANVAS)) {
            override fun pos0(): PointN = Program.mouseP
        }
        val graphics = object: AffineGraphics() {
            private val transform =
                AffineTransform {
                    var x = (it - drawPOS*layer.z)*scale
                    if(whole_draw) x = x.round(1.0)
                    x
                }

            override fun setStroke(size: Double) {
                gc.lineWidth = size*scale
            }

            override val image: ImageGraphics = object: ImageGraphics(transform) {
                override fun draw0(filename: String, pos: PointN, size: PointN) =
                    gc.drawImage(Image(filename), pos.X, pos.Y, size.X, size.Y)

                override fun draw0(image: Image, pos: PointN, size: PointN) = gc.drawImage(image, pos.X, pos.Y)

                override fun draw0(filename: String, pos: PointN) = gc.drawImage(Image(filename), pos.X, pos.Y)


                override fun draw0(image: Image, pos: PointN) = gc.drawImage(image, pos.X, pos.Y)

            }

            override val fill: GeometryGraphics = object: GeometryGraphics(transform) {
                override var color: Paint = Color(0.0, 0.0, 0.0, 1.0)
                    set(value) {
                        field = value
                        gc.fill = value
                    }

                override fun rect0(pos: PointN, size: PointN) = gc.fillRect(pos.X, pos.Y, size.X, size.Y)

                override fun oval0(pos: PointN, size: PointN) = gc.fillOval(pos.X, pos.Y, size.X, size.Y)

                override fun line0(pos1: PointN, pos2: PointN) = gc.strokeLine(pos1.X, pos1.Y, pos2.X, pos2.Y)

                override fun text0(pos: PointN, text: String) {
                    gc.textAlign = text_align
                    gc.font = font
                    gc.fillText(text, pos.X, pos.Y)
                }
            }
            override val stroke: GeometryGraphics = object: GeometryGraphics(transform) {
                override var color: Paint = Color(0.0, 0.0, 0.0, 1.0)
                    set(value) {
                        field = value
                        gc.stroke = value
                    }

                override fun rect0(pos: PointN, size: PointN) = gc.strokeRect(pos.X, pos.Y, size.X, size.Y)

                override fun oval0(pos: PointN, size: PointN) = gc.strokeOval(pos.X, pos.Y, size.X, size.Y)

                override fun line0(pos1: PointN, pos2: PointN) = gc.strokeLine(pos1.X, pos1.Y, pos2.X, pos2.Y)

                override fun text0(pos: PointN, text: String) {
                    gc.textAlign = text_align
                    gc.font = font
                    gc.strokeText(text, pos.X, pos.Y)
                }
            }
        }
    }
}
