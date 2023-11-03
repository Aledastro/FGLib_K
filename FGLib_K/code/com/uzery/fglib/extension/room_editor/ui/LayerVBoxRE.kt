package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight

class LayerVBoxRE(private val data: DataRE): VBox() {
    override val full: Int
        get() = data.layers.size+1

    override val rows: Int
        get() = data.layers.size+1

    override fun draw() {
        graphics.alpha = 0.3
        graphics.fill.rect(pos, size, Color.BEIGE)
        graphics.alpha = 1.0
        super.draw()
    }

    override val pos
        get() = (CANVAS-size)*PointN(0.5, 1.0)+PointN(0.0, -data.OFFSET/2)
    override val window: RectN
        get() = CANVAS_R
    override val sizeOne: PointN
        get() = PointN(78, 56)/Platform.scale

    override fun setNames(id: Int): String {
        return ""
    }

    override fun draw(pos: PointN, id: Int) {
        graphics.fill.font("TimesNewRoman", 22.0/2, FontWeight.EXTRA_BOLD)
        val name = when (id) {
            0 -> "ALL"
            else -> data.layers[id-1].name
        }
        graphics.fill.textC(pos+PointN(0, 4), name, Color.DARKBLUE)
    }
}