package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.room_editor.RoomEditorUI
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.paint.Color
import java.util.*

class InfoBoxRE(private val data: DataRE): InfoBox() {
    override val text_draw_offset: Double
        get() = 0.1

    override fun draw() {
        graphics.fill.rect(pos, size, FGUtils.transparent(Color.BEIGE, 0.5))
        super.draw()
    }

    private val obj_boxes = HashMap<Pair<GameObject, Room>, ObjectInfoBoxRE>()

    private fun getL(): List<String> {
        val res = LinkedList<String>()

        res.add("-----------------------------------------")
        res.add("")
        res.add("room: ${data.filenames[data.last_edit_n]}")
        res.add("")
        res.add("pos: ${data.last_edit_room.pos}")
        res.add("size: ${data.last_edit_room.size}")
        res.add("objects size: ${data.last_edit_room.objects.size}")
        for (index in 0 until BoundsBox.SIZE) {
            res.add("bounds[${BoundsBox.name(index)}]: ${WorldUtils.bs_n[data.last_edit_room]!![index]}")
        }

        res.add("")
        res.add("-----------------------------------------")
        res.add("")

        return res
    }

    override fun update() {
        WorldUtils.nextDebugForRoom(data.edit)

        fun setBoxesY() {
            var ss = 0.0
            obj_boxes.values.forEach { box ->
                box.pos.Y = pos.Y+origin_size.Y+ss
                ss += box.size.Y+10
            }
        }
        setBoxesY()

        fun addNew(pair: Pair<GameObject, Room>) {
            obj_boxes[pair] = ObjectInfoBoxRE(data, pair)
            obj_boxes[pair]!!.show()
        }

        data.select_objs.forEach { pair ->
            if (pair !in obj_boxes) {
                addNew(pair)
                RoomEditorUI.add(obj_boxes[pair]!!)
            }
        }
        val listToRemove = LinkedList<Pair<GameObject, Room>>()
        obj_boxes.forEach { (key, value) ->
            if (key !in data.select_objs || value.dead) listToRemove.add(key)
        }
        listToRemove.forEach { RoomEditorUI.remove(obj_boxes[it]!!) }
        data.select_objs.removeAll(listToRemove)
        listToRemove.forEach { obj_boxes.remove(it) }
    }

    override fun text(id: Int) = getL()[id]
    override val text_data_size: Int
        get() = getL().size

    override fun color(id: Int): Color {
        return when (id) {
            0 -> Color.gray(0.2, 0.9)
            else -> Color.gray(0.2, 0.9)
        }
    }

    override val pos
        get() = (CANVAS-origin_size).XP+PointN(-data.OFFSET, 70.0)

    val origin_size = PointN(350, 240)/Platform.scale
    override val size: PointN
        get() = origin_size+PointN(0.0, obj_boxes.values.sumOf { box -> box.size.Y+10 })


    override val window: RectN
        get() = CANVAS_R
}
