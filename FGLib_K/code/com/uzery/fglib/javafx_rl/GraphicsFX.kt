package com.uzery.fglib.javafx_rl

import com.uzery.fglib.core.realisation.FGGraphics
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.WINDOW
import com.uzery.fglib.javafx_rl.ProgramFX.gc
import com.uzery.fglib.utils.data.image.FGImage
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.graphics.AffineTransform
import com.uzery.fglib.utils.graphics.GeometryGraphics
import com.uzery.fglib.utils.graphics.ImageGraphics
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.min

internal object GraphicsFX: FGGraphics() {
    override var scale: Int = 1
        get() {
            return if (field == -1) {
                val size = WINDOW/CANVAS
                min(size.X.toInt(), size.Y.toInt())
            } else field
        }
    override var lineWidth = 1.0
        set(value) {
            gc.lineWidth = lineWidth
            field = value
        }

    override var lineDashOffset = 0.0
    override fun setLineDashes(vararg dashes: Double) {
        gc.setLineDashes(*dashes)
    }

    override var whole_draw = false

    override var global_view_scale = 1.0

    var oob = 0
    override val graphics = object: AffineGraphics() {
        private fun isOutOfBounds(pos: PointN, size: PointN): Boolean {
            val b = pos.more(Platform.CANVAS_REAL) || (pos+size).less(PointN.ZERO)
            if (b) oob++
            return b
        }

        private val gl_alpha
            get() = alpha

        private val transform =
            AffineTransform {
                var x = (it-drawPOS*layer.z)*scale
                if (whole_draw) x = x.roundC(1.0)
                x *= view_scale*global_view_scale
                x
            }
        private val transformSize = AffineTransform { it*scale*view_scale*global_view_scale }

        override fun setStroke(size: Double) {
            gc.lineWidth = transformSize.transform(PointN(size)).X
        }

        override val image: ImageGraphics = object: ImageGraphics(transform) {
            override fun draw0(image: FGImage, pos: PointN, size: PointN) {
                if (isOutOfBounds(pos, size)) return

                gc.globalAlpha = gl_alpha*this.alpha
                gc.drawImage(image.source, pos.X, pos.Y, size.X, size.Y)
            }
        }

        override val fill: GeometryGraphics = object: GeometryGraphics(transform, transformSize) {
            override fun rect0(pos: PointN, size: PointN, color: FGColor) {
                if (isOutOfBounds(pos, size)) return

                gc.fill = FGColor.fromFGColor(color)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.fillRect(pos.X, pos.Y, size.X, size.Y)
            }

            override fun oval0(pos: PointN, size: PointN, color: FGColor) {
                if (isOutOfBounds(pos, size)) return

                gc.fill = FGColor.fromFGColor(color)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.fillOval(pos.X, pos.Y, size.X, size.Y)
            }

            override fun line0(pos1: PointN, pos2: PointN, color: FGColor) {
                if (isOutOfBounds(pos1, pos2-pos1)) return

                gc.fill = FGColor.fromFGColor(color)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.strokeLine(pos1.X, pos1.Y, pos2.X, pos2.Y)
            }

            override fun text0(pos: PointN, text: String, color: FGColor) {
                if (isOutOfBounds(pos, text_size(text))) return

                gc.fill = FGColor.fromFGColor(color)
                gc.font = FGFont.fromFGFont(font)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.fillText(text, pos.X, pos.Y)
            }
        }
        override val stroke: GeometryGraphics = object: GeometryGraphics(transform, transformSize) {
            override fun rect0(pos: PointN, size: PointN, color: FGColor) {
                if (isOutOfBounds(pos, size)) return

                gc.stroke = FGColor.fromFGColor(color)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.strokeRect(pos.X, pos.Y, size.X, size.Y)
            }

            override fun oval0(pos: PointN, size: PointN, color: FGColor) {
                if (isOutOfBounds(pos, size)) return

                gc.stroke = FGColor.fromFGColor(color)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.strokeOval(pos.X, pos.Y, size.X, size.Y)
            }

            override fun line0(pos1: PointN, pos2: PointN, color: FGColor) {
                if (isOutOfBounds(pos1, pos2-pos1)) return

                gc.stroke = FGColor.fromFGColor(color)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.strokeLine(pos1.X, pos1.Y, pos2.X, pos2.Y)
            }

            override fun text0(pos: PointN, text: String, color: FGColor) {
                if (isOutOfBounds(pos, text_size(text))) return

                gc.stroke = FGColor.fromFGColor(color)
                gc.font = FGFont.fromFGFont(font)
                gc.globalAlpha = gl_alpha*this.alpha
                gc.strokeText(text, pos.X, pos.Y)
            }
        }
    }
}
