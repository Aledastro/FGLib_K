package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.CANVAS
import com.uzery.fglib.core.program.Platform.Companion.CANVAS_R
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.core.program.Platform.Companion.scale
import com.uzery.fglib.core.program.Program
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.ui.*
import com.uzery.fglib.utils.data.file.WriteData
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.ClassGetter
import javafx.scene.Cursor
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.util.*

class RoomEditor(private val filename: String, private val getter: ClassGetter<GameObject>): Extension {
    override fun children() = LinkedList<Extension>().apply { add(UIBox()) }

    private lateinit var edit: Room
    private val OFFSET = 40.0
    private val GRID
        get() = 32.0/scale
    private val GRID_P
        get() = PointN(GRID, GRID)

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
            //edit.objects.forEach { it.stats.POS /= 2 }
            WriteData.write(from(filename), edit.toString())
            println("saved")
        }
    }

    private fun clear() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.fill.rect(PointN.ZERO, CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }


    override fun init() {
        scale = 2
        World.getter = getter

        //todo
        val c = OneRoomController()
        World.init(c, filename)
        edit = World.rooms[0]
        c.room = edit

        Platform.whole_draw = true
        //todo
        draw_pos = Platform.options().size/2 - edit.size*PointN(1.0, 0.5)
        UIBox.add(canvasX, play_button, objects_vbox, layers_vbox, info_box)

        /*World.camera = object: Camera {
            override fun drawPOS(): PointN {
                return draw_pos + PointN(1.0, 1000.0) + Platform.CANVAS/2
            }
        }*/
        World.next()
    }

    private fun from(filename: String) = "project/media/$filename"

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var select_obj: GameObject? = null

    private var play_button = object: Button() {
        override val pos: PointN
            get() = CANVAS - PointN(110, 110)/scale

        override val size
            get() = PointN(52, 52)/scale
        override val window: RectN
            get() = CANVAS_R

        override val pressed: Boolean
            get() = keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.SPACE)

        override fun whenPressed(): String {
            World.next()
            return "- play -"
        }

        override fun whenNotPressed() = "- stop - "
    }

    private var objects_vbox = object: VBox(getter.entry_size(), 5) {
        override val pos = PointN(OFFSET, 170.0)
        override val window: RectN
            get() = CANVAS_R
        override val sizeOne: PointN
            get() = PointN(50, 50)/scale

        override fun setNames(id: Int): String {
            val o = getter.getEntry(id)
            o.setValues()
            return o.name
        }

        override fun draw(pos: PointN, id: Int) {
            getter.getEntry(id).draw(pos)
        }
    }

    private var canvasX = object: UICanvas() {
        override val pos: PointN
            get() = PointN.ZERO
        override val size: PointN
            get() = CANVAS
        override val window: RectN
            get() = CANVAS_R

        var draw_bounds = false

        override fun draw() {
            World.draw(draw_pos - edit.pos)

            if(draw_bounds) WorldUtils.drawBounds(edit, draw_pos)

            drawLines()

            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.stroke.rect(draw_pos, CANVAS, Color.DARKBLUE)
        }

        private fun drawLines() {
            val c = FGUtils.transparent(Color.WHITE, 0.1)
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.setStroke(1.0)
            for(i in 0..(window.S.Y/GRID + 1).toInt()) {
                graphics.stroke.line(
                    -GRID_P + draw_pos.mod(GRID)
                            + PointN(0.0, i*GRID), PointN(window.S.X + GRID, 0.0), c)
            }
            for(i in 0..(window.S.X/GRID + 1).toInt()) {
                graphics.stroke.line(
                    -GRID_P + draw_pos.mod(GRID)
                            + PointN(i*GRID, 0.0), PointN(0.0, window.S.Y + GRID), c)
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
            if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds
        }

        var lastPOS = PointN.ZERO
        private fun checkForAdd() {
            if(mouse_keys.pressed(MouseButton.PRIMARY)) {
                val o = getter.getEntry(objects_vbox.select)
                val pp = (mouse.pos()/scale - draw_pos).round(GRID) + GRID_P/2
                if(lastPOS == pp) return
                o.stats.POS = pp
                lastPOS = pp
                edit.objects.add(o)
                select_obj = o
            }
        }

        private fun checkForRemove() {
            if(mouse_keys.pressed(MouseButton.SECONDARY)) {
                edit.objects.removeIf { o -> (o.stats.POS.lengthTo(mouse.pos()/scale - draw_pos))<GRID/2 }
                if(!edit.objects.contains(select_obj)) select_obj = null
            }
        }

        private fun checkForMove(): Boolean {
            if(!keyboard.pressed(KeyCode.SPACE)) {
                Program.cursor = Cursor.DEFAULT
                return false
            }
            if(mouse_keys.anyPressed(*MouseButton.values())) draw_pos += (mouse.pos() - last_mouse_pos)/scale
            Program.cursor = Cursor.CLOSED_HAND

            return true
        }
    }

    private var layers_vbox = object: VBox(12, 12) {
        override val pos
            get() = (CANVAS - size)*PointN(0.5, 1.0) + PointN(0.0, -OFFSET)
        override val window: RectN
            get() = CANVAS_R
        override val sizeOne: PointN
            get() = PointN(50, 50)/scale

        override fun setNames(id: Int): String {
            return when(id) {
                0 -> "ALL"
                full - 1 -> "etc layers"
                else -> "layer $id"
            }
        }

        override fun draw(pos: PointN, id: Int) {
            graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 22.0)
            val name = when(id) {
                0 -> "ALL"
                full - 1 -> "R"
                else -> "L$id"
            }
            graphics.fill.textC(pos + PointN(0, 9), name, Color.DARKBLUE)
        }
    }

    private var info_box = object: InfoBox() {
        private fun getL(): LinkedList<String> {
            val res = LinkedList<String>()

            val s = select_obj.toString()
            val index = s.indexOf(':')
            if(index == -1) {
                res.add("object: $s")
                return res
            }

            val name = s.substring(0, index)
            val args = s.substring(index + 1)
            val t = StringTokenizer(args, "]")

            res.add("object: $name")
            while(t.hasMoreTokens()) {
                res.add((t.nextToken() + "]\n").substring(1))
            }
            return res
        }

        override fun text(id: Int) = getL()[id]
        override val text_size: Int
            get() = getL().size

        override fun color(id: Int): Color {
            return when(id) {
                0 -> Color.PURPLE
                else -> Color.PURPLE
            }
        }

        override val pos
            get() = (CANVAS - size).XP + PointN(-OFFSET, 170.0)
        override val size
            get() = PointN(350, 400)/scale
        override val window: RectN
            get() = CANVAS_R
    }
}
