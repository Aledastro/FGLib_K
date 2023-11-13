package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.room_editor.RoomEditorUI
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.paint.Color
import java.util.*
import kotlin.collections.HashMap

class InfoBoxRE(private val data: DataRE): InfoBox() {
    override val text_draw_offset: Double
        get() = 0.1

    override fun draw() {
        graphics.alpha = 0.3
        graphics.fill.rect(pos, size, Color.BEIGE)
        graphics.alpha = 1.0
        super.draw()
    }

    private val obj_boxes = HashMap<GameObject, ObjectInfoBoxRE>()

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

        fun setBoxesY(){
            var ss = 0.0
            obj_boxes.values.forEach { box ->
                box.pos.Y = pos.Y + origin_size.Y + ss
                ss += box.size.Y + 10
            }
        }
        setBoxesY()

        fun addNew(o: GameObject){
            obj_boxes[o] = ObjectInfoBoxRE(data, o)
            obj_boxes[o]!!.show()
        }

        data.select_objs.forEach { o ->
            if(o !in obj_boxes){
                addNew(o)
                RoomEditorUI.add(obj_boxes[o]!!)
            }
        }
        val listToRemove = LinkedList<GameObject>()
        obj_boxes.forEach { (key, value) ->
            if(key !in  data.select_objs || value.dead) listToRemove.add(key)
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
            0 -> Color.PURPLE
            else -> Color.PURPLE
        }
    }

    override val pos
        get() = (CANVAS-origin_size).XP+PointN(-data.OFFSET, 70.0)

    val origin_size = PointN(350, 240)/Platform.scale
    override val size: PointN
        get() = origin_size + PointN(0.0, obj_boxes.values.sumOf { box -> box.size.Y + 10 })


    override val window: RectN
        get() = CANVAS_R
}
