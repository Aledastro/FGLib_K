package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.room_editor.RoomEditorUI
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

class InfoBoxRE(private val data: DataRE): InfoBox() {
    override val text_draw_offset: Double
        get() = 0.1

    override fun draw() {
        graphics.fill.rect(pos, size, FGUtils.transparent(Color.BEIGE, 0.5))
        super.draw()
    }

    val obj_boxes = HashMap<Pair<GameObject, Room>, ObjectInfoBoxRE>()

    override fun getL(): ArrayList<String> {
        val res = ArrayList<String>()

        res.add("-----------------------------------------")
        res.add("")

        if (world_info) {
            res.add("world")
            res.add("")
            res.add("rooms size: ${data.filenames.size}")
            res.add("objects size: ${World.rooms.sumOf { it.objects.size }}")
            res.add("* active: ${World.rooms.sumOf { r -> r.objects.count { !it.tagged("#inactive") } }}")
            res.add("* movable: ${World.rooms.sumOf { r -> r.objects.count { !it.tagged("#immovable") } }}")
            for (index in 0 until BoundsBox.SIZE) {
                res.add("bounds[${BoundsBox.name(index)}]: ${World.rooms.sumOf { WorldUtils.bs_n[it]!![index] }}")
            }
        }

        if (world_info && room_info) {
            res.add("")
            res.add("-----------------------------------------")
            res.add("")
        }

        if (room_info) {
            res.add("room: ${data.filenames[data.last_edit_n]}")
            res.add("")
            res.add("pos: ${data.last_edit_room.pos}")
            res.add("size: ${data.last_edit_room.size}")
            res.add("objects size: ${data.last_edit_room.objects.size}")
            res.add("* active: ${data.last_edit_room.objects.count { !it.tagged("#inactive") }}")
            res.add("* movable: ${data.last_edit_room.objects.count { !it.tagged("#immovable") }}")
            for (index in 0 until BoundsBox.SIZE) {
                res.add("bounds[${BoundsBox.name(index)}]: ${WorldUtils.bs_n[data.last_edit_room]!![index]}")
            }
        }

        res.add("")
        res.add("-----------------------------------------")

        return res
    }

    private var world_info = true
    private var room_info = false

    override fun update() {
        super.update()
        World.rooms.forEach { WorldUtils.nextDebugForRoom(it) }

        if (keyboard.inPressed(KeyCode.F5)) {
            world_info = !world_info
        }
        if (keyboard.inPressed(KeyCode.F6)) {
            room_info = !room_info
        }

        fun addNew(pair: Pair<GameObject, Room>) {
            obj_boxes[pair] = ObjectInfoBoxRE(data, pair)
            obj_boxes[pair]!!.update()
            obj_boxes[pair]!!.show()
        }

        fun setBoxesY() {
            var ss = 0.0
            obj_boxes.values.forEach { box ->
                box.pos.Y = pos.Y+origin_size.Y+ss

                if(box.pos.Y < CANVAS.Y) box.show()
                else box.hide()

                ss += box.size.Y+10
            }
        }

        data.select_objs.forEach { pair ->
            if (pair !in obj_boxes) {
                addNew(pair)
                RoomEditorUI.add(obj_boxes[pair]!!)
            }
        }
        val listToRemove = ArrayList<Pair<GameObject, Room>>()
        obj_boxes.forEach { (key, value) ->
            if (key !in data.select_objs || value.dead) listToRemove.add(key)
        }
        listToRemove.forEach { RoomEditorUI.remove(obj_boxes[it]!!) }
        data.select_objs.removeAll(listToRemove.toSet())
        listToRemove.forEach { obj_boxes.remove(it) }

        setBoxesY()
    }

    override fun color(id: Int): Color {
        return when (id) {
            0 -> Color.gray(0.2, 0.9)
            else -> Color.gray(0.2, 0.9)
        }
    }

    override val pos
        get() = (CANVAS-origin_size).XP+PointN(-data.OFFSET, 70.0)

    val origin_size
        get() = PointN(186.0, y_size*(text_data_size+1.5))
    override val size: PointN
        get() = origin_size+PointN(0.0, obj_boxes.values.sumOf { box -> box.size.Y+10 })


    override val window: RectN
        get() = CANVAS_R
}
