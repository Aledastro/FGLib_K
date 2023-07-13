package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.scale
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.input.KeyCode

class ObjectVBoxRE(private val input: DataGetterRE): VBox(0, 5) {
    val data: DataRE
        get() = input.get()

    val select_obj
        get() = data.getter.getEntry(chosen())()

    override fun update() {
        if(keyboard.inPressed(KeyCode.DOWN)) {
            data.groupsSelect[select]++
        }
        if(keyboard.inPressed(KeyCode.UP)) {
            data.groupsSelect[select]--
        }
        coerceGroups()
    }

    override fun ifActiveUpdate() {

    }

    private fun coerceGroups() {
        for(i in data.groupsSelect.indices) {
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
        get() = PointN(50, 50)/scale

    override fun setNames(id: Int): String {
        return data.names[id].s
    }

    override fun draw(pos: PointN, id: Int) {
        val obj = data.getter.getEntry(from(id))()
        obj.draw(pos)
        if(data.draw_bounds){
            WorldUtils.drawBoundsFor(obj, pos, BoundsBox.index("RED"))
            WorldUtils.drawBoundsFor(obj, pos, BoundsBox.index("ORANGE"))
        }
    }

    fun chosen(): Int {
        return from(select)
    }

    private fun from(i: Int): Int {
        return data.ids[data.groupsValues[i][data.groupsSelect[i]]]!!
    }
}