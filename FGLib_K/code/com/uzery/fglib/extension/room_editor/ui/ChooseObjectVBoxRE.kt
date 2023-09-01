package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.paint.Color
import kotlin.math.min

class ChooseObjectVBoxRE(private val data: DataRE): VBox() {
    override val full: Int
        get() = data.groupsValues[data.select_group].size

    override val rows: Int
        get() = min(10, data.groupsValues[data.select_group].size)

    override val pos
        get() = (Platform.CANVAS-size)/2
    override val window: RectN
        get() = Platform.CANVAS_R
    override val sizeOne: PointN
        get() = PointN(60, 60)/Platform.scale

    override fun setNames(id: Int): String {
        return "$id"
    }

    override fun draw() {
        Platform.graphics.alpha = 0.7
        Platform.graphics.fill.rect(pos, size, Color.BEIGE)
        Platform.graphics.alpha = 1.0
        super.draw()
    }

    override fun draw(pos: PointN, id: Int) {
        val obj = data.getter.getEntry(from(id))()
        obj.draw(pos)
        if (data.draw_bounds) {
            WorldUtils.drawBoundsFor(obj, pos, BoundsBox.RED)
            WorldUtils.drawBoundsFor(obj, pos, BoundsBox.ORANGE)
        }
    }

    private fun from(i: Int): Int {
        return data.ids[data.groupsValues[data.select_group][i]]!!
    }
}