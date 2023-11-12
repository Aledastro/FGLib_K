package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.program.Platform.scale
import com.uzery.fglib.core.world.OneRoomWC
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.ui.*
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.getter.AbstractClassGetter
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import java.util.*

class RoomEditor(private var getter: (Int) -> Pair<AbstractClassGetter<GameObject>, Array<String>>):
    Extension(RoomEditorUI) {
    private lateinit var world_save: Array<String>
    private lateinit var data: DataRE

    private lateinit var play_button: PlayButtonRE
    private lateinit var objects_vbox: ObjectVBoxRE
    private lateinit var choose_objects_vbox: ChooseObjectVBoxRE
    private lateinit var canvasX: CanvasRE
    private lateinit var layers_vbox: LayerVBoxRE
    private lateinit var info_box: InfoBoxRE

    override fun init() {
        data = DataRE(getter(0))

        play_button = PlayButtonRE(data)
        objects_vbox = ObjectVBoxRE(data)
        choose_objects_vbox = ChooseObjectVBoxRE(data)
        canvasX = CanvasRE(data)
        layers_vbox = LayerVBoxRE(data)
        info_box = InfoBoxRE(data)

        scale = 2
        World.getter = data.getter

        //todo
        val c = OneRoomWC()
        World.init(c, *data.filenames)
        data.edit = World.rooms[data.edit_n]
        c.room = data.edit

        Platform.whole_draw = true
        //todo
        data.draw_pos = (Platform.options().size-data.edit.size)/2

        play_button = PlayButtonRE(data)
        objects_vbox = ObjectVBoxRE(data)
        choose_objects_vbox = ChooseObjectVBoxRE(data)
        canvasX = CanvasRE(data)
        layers_vbox = LayerVBoxRE(data)
        info_box = InfoBoxRE(data)

        RoomEditorUI.clear()
        RoomEditorUI.add(canvasX, play_button, objects_vbox, layers_vbox, info_box, choose_objects_vbox)
        canvasX.show()
        play_button.show()
        objects_vbox.show()
        layers_vbox.show()
        info_box.show()

        World.next() //todo why it needed?
        data.init()

        world_save = Array(World.rooms.size) { World.rooms[it].toString() }
    }

    override fun update() {
        data.edit = World.rooms[data.edit_n]
        data.last_edit_room = data.edit

        data.select_layer = layers_vbox.select

        if (data.select_group != objects_vbox.select) {
            data.select_group = objects_vbox.select
            choose_objects_vbox.select = data.groupsSelect[data.select_group]
        }
        data.chosen_entry = objects_vbox.chosenEntry()
        data.chosen_obj = data.getter.getEntry(data.chosen_entry)()


        if (keyboard.pressed(KeyCode.SHIFT)) {
            choose_objects_vbox.show()
        } else {
            choose_objects_vbox.hide()
        }
        data.groupsSelect[data.select_group] = choose_objects_vbox.select

        if (data.hide_ui) {
            objects_vbox.hide()
            layers_vbox.hide()
            info_box.hide()
            choose_objects_vbox.hide()
        } else {
            objects_vbox.show()
            layers_vbox.show()
            info_box.show()
        }

        play_button.action()
        if (data.world_play) {
            if (!data.last_world_play) {
                world_save = Array(World.rooms.size) { World.rooms[it].toString() }
            }
            World.next()
        } else if (data.last_world_play) {
            for (i in World.rooms.indices) {
                World.rooms[i] = WorldUtils.readInfo(world_save[i].split("\n"))
            }
        }//todo do more simple way
        data.last_world_play = data.world_play

        checkForSave()

        setCurrentLayers()

        data.time++
    }

    private fun setCurrentLayers() {
        val map = HashMap<String, DrawLayer>()
        World.active_rooms.forEach { r ->
            r.objects.forEach { o ->
                o.visuals.forEach {
                    val layer = it.drawLayer()
                    map[layer.name] = layer
                }
            }
        }
        val list = LinkedList<DrawLayer>()
        list.addAll(map.values.toList())
        list.sortBy { o -> o.sort }
        data.layers = list
    }

    private fun checkForSave() {
        if (keyboard.allPressed(KeyCode.CONTROL, KeyCode.SHIFT) && keyboard.inPressed(KeyCode.S)) {
            //edit.objects.forEach { it.stats.POS /= 2 }
            data.filenames.indices.forEach { i -> TextData.write(data.filenames[i], World.rooms[i].toString()) }
            println("saved")
        }
    }

    override fun draw(pos: PointN) {
        clear()
    }

    private fun clear() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.alpha = 1.0
        Platform.global_alpha = 1.0
        graphics.fill.draw(CANVAS_R, Color(0.7, 0.6, 0.9, 1.0))
    }
}
