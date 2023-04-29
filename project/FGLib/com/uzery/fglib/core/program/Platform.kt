package com.uzery.fglib.core.program

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

        var whole_draw = false
        val CANVAS
            get() = PointN(options().size.X, options().size.Y)

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
                AffineTransform { p -> if(whole_draw) (p - drawPOS*layer.z).round(1.0) else (p - drawPOS*layer.z) }

            override fun setStroke(size: Double) {
                Program.gc.lineWidth = size
            }

            override val image: ImageGraphics = object: ImageGraphics(transform) {
                override fun draw0(filename: String, pos: PointN, size: PointN) {
                    Program.gc.drawImage(Image(filename), pos.X, pos.Y)
                }

                override fun draw0(image: Image, pos: PointN, size: PointN) {
                    Program.gc.drawImage(image, pos.X, pos.Y)
                }

                override fun draw0(filename: String, pos: PointN) {
                    Program.gc.drawImage(Image(filename), pos.X, pos.Y)
                }

                override fun draw0(image: Image, pos: PointN) {
                    Program.gc.drawImage(image, pos.X, pos.Y)
                }
            }

            override val fill: GeometryGraphics = object: GeometryGraphics(transform) {
                override var color: Paint = Color(0.0, 0.0, 0.0, 1.0)
                    set(value) {
                        field = value
                        Program.gc.fill = value
                    }

                override fun rect0(pos: PointN, size: PointN) {
                    Program.gc.fillRect(pos.X, pos.Y, size.X, size.Y)
                }

                override fun oval0(pos: PointN, size: PointN) {
                    Program.gc.fillOval(pos.X, pos.Y, size.X, size.Y)
                }

                override fun line0(pos1: PointN, pos2: PointN) {
                    Program.gc.strokeLine(pos1.X, pos1.Y, pos2.X, pos2.Y)
                }

                override fun text0(pos: PointN, text: String) {
                    Program.gc.fillText(text, pos.X, pos.Y)
                }
            }
            override val stroke: GeometryGraphics = object: GeometryGraphics(transform) {
                override var color: Paint = Color(0.0, 0.0, 0.0, 1.0)
                    set(value) {
                        field = value
                        Program.gc.stroke = value
                    }

                override fun rect0(pos: PointN, size: PointN) {
                    Program.gc.strokeRect(pos.X, pos.Y, size.X, size.Y)
                }

                override fun oval0(pos: PointN, size: PointN) {
                    Program.gc.strokeOval(pos.X, pos.Y, size.X, size.Y)
                }

                override fun line0(pos1: PointN, pos2: PointN) {
                    Program.gc.strokeLine(pos1.X, pos1.Y, pos2.X, pos2.Y)
                }

                override fun text0(pos: PointN, text: String) {
                    Program.gc.strokeText(text, pos.X, pos.Y)
                }
            }
        }
    }
}