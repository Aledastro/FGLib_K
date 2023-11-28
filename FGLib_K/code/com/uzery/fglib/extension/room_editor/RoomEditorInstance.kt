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

class RoomEditorInstance(private var getter: Pair<AbstractClassGetter<GameObject>, Array<String>>):
    Extension() {
    private lateinit var world_save: Array<String>
    private lateinit var data: DataRE

    private lateinit var play_button: PlayButtonRE
    private lateinit var choose_group_panel: ChooseGroupPanelRE
    private lateinit var choose_objects_panel: ChooseObjectPanelRE
    private lateinit var edit_canvas: CanvasRE
    private lateinit var layers_panel: LayerPanelRE
    private lateinit var info_box: InfoBoxRE
    private lateinit var redact_field: RedactTextFieldRE
    private lateinit var help_box: HelpBoxRE
    private lateinit var status_box: StatusBoxRE

    private var ui = RoomEditorUI()

    init {
        add(ui)
    }

    override fun init() {
        data = DataRE(getter)
        data.ui = ui

        scale = 2
        World.getter = data.getter

        val c = OneRoomWC()
        World.init(c, *data.filenames)
        data.edit = World.rooms[data.edit_n]
        c.room = data.edit

        Platform.whole_draw = true
        //todo
        data.draw_pos = (Platform.options().size-data.edit.size)/2

        play_button = PlayButtonRE(data)
        choose_group_panel = ChooseGroupPanelRE(data)
        choose_objects_panel = ChooseObjectPanelRE(data)
        edit_canvas = CanvasRE(data)
        layers_panel = LayerPanelRE(data)
        info_box = InfoBoxRE(data)
        redact_field = RedactTextFieldRE(data)
        help_box = HelpBoxRE(data)
        status_box = StatusBoxRE(data)

        ui.add(
            edit_canvas, play_button, choose_group_panel, layers_panel,
            info_box, choose_objects_panel, redact_field, help_box, status_box
        )

        edit_canvas.show()
        play_button.show()
        choose_group_panel.show()
        layers_panel.show()
        info_box.show()
        status_box.show()

        redact_field.hide()

        World.next() //todo why it needed?
        data.init()

        world_save = Array(World.rooms.size) { World.rooms[it].toString() }
    }

    override fun update() {
        data.redact_field_active = redact_field.showing

        data.edit = World.rooms[data.edit_n]
        data.last_edit_room = data.edit

        data.select_layerID = layers_panel.select

        if (data.select_group != choose_group_panel.select) {
            data.select_group = choose_group_panel.select
            choose_objects_panel.select = data.groupsSelect[data.select_group]
        }
        data.chosen_entry = choose_group_panel.chosenEntry()
        data.chosen_obj = data.getter.getEntry(data.chosen_entry)()

        fun setVisibility() {
            if (keyboard.pressed(KeyCode.SHIFT) && !keyboard.pressed(KeyCode.CONTROL) && !redact_field.showing) {
                choose_objects_panel.show()
            } else {
                choose_objects_panel.hide()
            }

            if (data.hide_ui) {
                choose_group_panel.hide()
                layers_panel.hide()
                info_box.hide()
                choose_objects_panel.hide()
                info_box.obj_boxes.values.forEach { it.hide() }
            } else {
                choose_group_panel.show()
                layers_panel.show()
                info_box.show()
                info_box.obj_boxes.values.forEach { it.show() }
            }

            if(data.time-data.save_time<status_box.DELAY) status_box.show()
            else status_box.hide()

            if (data.redact_pair != null) redact_field.show()
            else redact_field.hide()

            if (keyboard.pressed(KeyCode.F1)) help_box.show()
            else help_box.hide()
        }
        setVisibility()

        data.groupsSelect[data.select_group] = choose_objects_panel.select

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
        val list = ArrayList<DrawLayer>()
        list.addAll(map.values.toList())
        list.sortBy { o -> o.sort }
        data.layers = list
    }

    private fun checkForSave() {
        if (keyboard.allPressed(KeyCode.CONTROL, KeyCode.SHIFT) && keyboard.inPressed(KeyCode.S)) {
            //edit.objects.forEach { it.stats.POS /= 2 }
            data.filenames.indices.forEach { i -> TextData.write(data.filenames[i], World.rooms[i].toString()) }
            data.save_time = data.time
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
