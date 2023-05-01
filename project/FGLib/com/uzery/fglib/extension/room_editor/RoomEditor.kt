package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.core.world.World
import com.uzery.fglib.extension.ui.Button
import com.uzery.fglib.extension.ui.UIBox
import com.uzery.fglib.extension.ui.UICanvas
import com.uzery.fglib.extension.ui.VBox
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import java.util.*

class RoomEditor(private val getter: ClassGetter<GameObject>): Extension {
    override fun children() = LinkedList<Extension>().apply { add(UIBox()) }

    private val GRID = 32.0
    private val GRID_P = PointN(GRID, GRID)
    private lateinit var room_pos: PointN
    private var world = World(getter)

    private var draw_pos = PointN.ZERO

    override fun update() {
        clear()
        next()
    }

    private fun next() {
        play_button.action()
        Platform.update()
        checkForSave()

        //world.room.objs().removeIf {o->o.tagged("player")}
    }

    private fun checkForSave() {
        if(keyboard.allPressed(KeyCode.CONTROL, KeyCode.SHIFT) && keyboard.inPressed(KeyCode.S)) {
            WriteData.write("project/media/1.room", World.room.toString())
            println("saved")
        }
    }

    private fun clear() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.fill.rect(PointN.ZERO, Platform.CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }


    override fun init() {
        world.init("project/media/1.room")
        Platform.whole_draw = true
        room_pos = Platform.CANVAS/2 - World.room.size/2
        UIBox.add(canvasX, play_button, objects_vbox)

        /*World.camera = object: Camera {
            override fun drawPOS(): PointN {
                return draw_pos + PointN(1.0, 1000.0) + Platform.CANVAS/2
            }
        }*/
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var play_button = object: Button() {
        override val pressed: Boolean
            get() = keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.SPACE)

        override fun whenPressed(): String {
            World.next()
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
            World.draw(room_pos + draw_pos)

            drawLines()

            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.stroke.rect(room_pos + draw_pos - World.room.pos, Platform.CANVAS, Color.DARKBLUE)
        }

        private fun drawLines() {
            val c = Color.WHITE.interpolate(Color.TRANSPARENT, 0.9)
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            for(i in -70..70) {
                graphics.setStroke(1.0)
                val pp = room_pos - World.room.pos + draw_pos.transform { x -> MathUtils.mod(x, GRID) }
                graphics.stroke.line(pp + PointN(-Platform.CANVAS.X, i*GRID), pp + PointN(Platform.CANVAS.X, i*GRID), c)
                graphics.stroke.line(pp + PointN(i*GRID, -Platform.CANVAS.Y), pp + PointN(i*GRID, Platform.CANVAS.Y), c)
            }
        }

        override fun ifActive() {
            if(checkForMove()) {
                return
            }
            checkForAdd()
            checkForRemove()
        }

        private var last_mouse_pos = PointN.ZERO

        override fun update() {
            last_mouse_pos = mouse.pos()
        }

        var lastPOS = PointN.ZERO
        private fun checkForAdd() {
            if(mouse_keys.pressed(MouseButton.PRIMARY)) {
                val o = getter.getEntry(objects_vbox.select)
                val pp = (mouse.pos() - room_pos - World.room.pos - draw_pos).round(GRID) + GRID_P/2
                if(lastPOS == pp) return
                o.stats.POS = pp
                lastPOS = pp
                World.room.add(o)
            }
        }

        private fun checkForRemove() {
            if(mouse_keys.pressed(MouseButton.SECONDARY)) {
                World.room.objects.removeIf { o -> (o.stats.POS - (mouse.pos() - room_pos - World.room.pos - draw_pos)).length()<GRID/2 }
            }
        }

        private fun checkForMove(): Boolean {
            if(keyboard.pressed(KeyCode.SPACE) && mouse_keys.pressed(MouseButton.PRIMARY)) {
                draw_pos += mouse.pos() - last_mouse_pos
                return true
            }
            return false
        }
    }
}
