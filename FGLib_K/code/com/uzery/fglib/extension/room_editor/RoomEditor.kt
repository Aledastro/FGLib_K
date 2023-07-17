package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.CANVAS
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.scale
import com.uzery.fglib.core.world.World
import com.uzery.fglib.extension.room_editor.ui.*
import com.uzery.fglib.extension.ui.UIBox
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import java.util.*

class RoomEditor(private val getter: ClassGetter<GameObject>, private vararg val filenames: String): Extension {
    override fun children() = LinkedList<Extension>().apply { add(UIBox()) }

    private val data = DataRE(getter, filenames)

    fun setLayers(list: LinkedList<DrawLayer>) {
        data.layers = list
    }

    override fun update() {
        clear()
        next()
    }

    private fun next() {
        data.edit = World.rooms[data.edit_n]
        data.last_edit_room = data.edit

        data.select_layer = layers_vbox.select
        data.chosen = objects_vbox.chosen()

        if(data.hide_ui) {
            objects_vbox.hide()
            layers_vbox.hide()
            info_box.hide()
        } else {
            objects_vbox.show()
            layers_vbox.show()
            info_box.show()
        }

        play_button.action()
        Platform.update()
        checkForSave()
    }

    private fun checkForSave() {
        if(keyboard.allPressed(KeyCode.CONTROL, KeyCode.SHIFT) && keyboard.inPressed(KeyCode.S)) {
            //edit.objects.forEach { it.stats.POS /= 2 }
            filenames.indices.forEach { i -> WriteData.write(filenames[i], World.rooms[i].toString()) }
            println("saved")
        }
    }

    private fun clear() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.alpha = 1.0
        Platform.global_alpha = 1.0
        graphics.fill.rect(PointN.ZERO, CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }


    override fun init() {
        scale = 2
        World.getter = getter

        //todo
        val c = OneRoomController()
        World.init(c, *filenames)
        data.edit = World.rooms[data.edit_n]
        c.room = data.edit

        Platform.whole_draw = true
        //todo
        data.draw_pos = Platform.options().size/4 - data.edit.size*PointN(0.5, 0.5)
        UIBox.add(canvasX, play_button, objects_vbox, layers_vbox, info_box)
        canvasX.show()
        play_button.show()
        objects_vbox.show()
        layers_vbox.show()
        info_box.show()

        /*World.camera = object: Camera {
            override fun drawPOS(): PointN {
                return draw_pos + PointN(1.0, 1000.0) + Platform.CANVAS/2
            }
        }*/
        World.next()
        data.init()
    }

    private var play_button = PlayButtonRE()
    private var objects_vbox = ObjectVBoxRE(data)
    private var canvasX = CanvasRE(data)
    private var layers_vbox = LayerVBoxRE(data)
    private var info_box = InfoBoxRE(data)
}
