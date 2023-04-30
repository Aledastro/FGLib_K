package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.core.world.World
import com.uzery.fglib.extension.ui.Button
import com.uzery.fglib.extension.ui.UIBox
import com.uzery.fglib.extension.ui.UICanvas
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import java.util.*

class RoomEditor(private val getter: ClassGetter<GameObject>): Extension {
    override fun children() = LinkedList<Extension>().apply { add(UIBox()) }

    private lateinit var room_pos: PointN
    private var world = World(getter)

    override fun update() {
        clear()
        next()
    }

    private fun next() {
        play_button.action()
        Platform.update()

        //world.room.objs().removeIf {o->o.tagged("player")}
    }

    private fun clear() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.fill.rect(PointN.ZERO, Platform.CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }

    override fun init() {
        world.init("project/media/1.map")
        Platform.whole_draw = true
        room_pos = Platform.CANVAS/2 - World.room.size/2
        UIBox.add(canvasX, play_button, objects_vbox)
    }

    private var play_button = object: Button() {
        override val pressed: Boolean
            get() = Platform.keyboard.inPressed(KeyCode.SPACE)

        override fun whenPressed(): String {
            World.room.next()
            return "- play -"
        }

        override fun whenNotPressed() = "- stop - "
        override val pos: PointN
            get() = Platform.CANVAS - PointN(90.0, 90.0)

        override val size = PointN(42.0, 42.0)
    }

    private var objects_vbox = object: VBox(getter.entry_size(), 5) {
        override fun setNames(id: Int): String {
            val o = getter.getEntry(id)
            o.setValues()
            return o.name
        }

        override fun draw(pos: PointN, id: Int) {
            getter.getEntry(id).draw(pos)
        }

        override val pos: PointN
            get() = PointN(70.0, 170.0)
    }
    private var canvasX = object: UICanvas() {
        override val pos: PointN
            get() = PointN.ZERO
        override val size: PointN
            get() = Platform.CANVAS

        override fun draw() {
            World.room.draw(room_pos)

            graphics.layer = DrawLayer.CAMERA_OFF
            graphics.stroke.rect(room_pos, World.room.size, Color.DARKBLUE)

            drawLines()
        }

        private fun drawLines() {
            val c = Color.WHITE.interpolate(Color.TRANSPARENT, 0.9)
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            for(i in -70..70) {
                graphics.setStroke(1.0)
                graphics.stroke.line(
                    room_pos + PointN(-Platform.CANVAS.X, i*32.0),
                    room_pos + PointN(Platform.CANVAS.X, i*32.0),
                    c)
                graphics.stroke.line(
                    room_pos + PointN(i*32.0, -Platform.CANVAS.Y),
                    room_pos + PointN(i*32.0, Platform.CANVAS.Y),
                    c)
            }

        }

        override fun update() {
            checkForAdd()
            checkForRemove()
        }

        var lastPOS = PointN.ZERO
        private fun checkForAdd() {
            if(mouse_keys.pressed(MouseButton.PRIMARY)) {
                val o = getter.getEntry(objects_vbox.select)
                val pp = (mouse.pos() - room_pos).round(32.0) + PointN(16.0, 16.0)
                if(lastPOS == pp) return
                o.stats.POS = pp
                lastPOS = pp
                World.room.add(o)
            }
        }

        private fun checkForRemove() {
            if(mouse_keys.pressed(MouseButton.SECONDARY)) {
                World.room.objects.removeIf { o -> (o.stats.POS - (mouse.pos() - room_pos)).length()<16.0 }
            }
        }
    }
}
