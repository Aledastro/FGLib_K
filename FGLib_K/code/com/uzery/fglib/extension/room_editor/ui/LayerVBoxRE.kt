package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.util.*

class LayerVBoxRE(private val data: DataRE): VBox(0, 24){
    override val full: Int
        get() = data.layers.size + 1

    override val rows: Int
        get() = data.layers.size + 1

    override val pos
        get() = (Platform.CANVAS - size)*PointN(0.5, 1.0) + PointN(0.0, -data.OFFSET/2)
    override val window: RectN
        get() = Platform.CANVAS_R
    override val sizeOne: PointN
        get() = PointN(78, 56)/Platform.scale

    override fun setNames(id: Int): String {
        return ""
    }

    override fun draw(pos: PointN, id: Int) {
        Platform.graphics.fill.font = Font.font("TimesNewRoman", FontWeight.EXTRA_BOLD, 22.0)
        val name = when(id) {
            0 -> "ALL"
            else -> data.layers[id - 1].name
        }
        Platform.graphics.fill.textC(pos + PointN(0, 4), name, Color.DARKBLUE)
    }
}