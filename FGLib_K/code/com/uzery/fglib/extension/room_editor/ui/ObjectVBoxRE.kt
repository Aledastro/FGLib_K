package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.scale
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.paint.Color

class ObjectVBoxRE(private val data: DataRE): VBox() {

    override val rows = 5

    override fun update() {
        super.update()
        coerceGroups()
    }

    override fun ifActiveUpdate() {

    }

    private fun coerceGroups() {
        for (i in data.groupsSelect.indices) {
            val size = data.groupsValues[i].size
            data.groupsSelect[i] = data.groupsSelect[i].coerceIn(0 until size)
        }
    }

    override val full
        get() = data.groupsValues.size

    override val pos
        get() = PointN(data.OFFSET, 70.0)
    override val window: RectN
        get() = Platform.CANVAS_R
    override val sizeOne: PointN
        get() = PointN(60, 60)/scale

    override fun setNames(id: Int): String {
        return data.names[id].s
    }

    override fun draw() {
        Platform.graphics.alpha = 0.3
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

    fun chosenEntry(): Int {
        return from(select)
    }

    private fun from(i: Int): Int {
        return data.ids[data.groupsValues[i][data.groupsSelect[i]]]!!
    }
}