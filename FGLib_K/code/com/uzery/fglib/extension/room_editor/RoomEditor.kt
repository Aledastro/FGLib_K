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
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.num.StringN
import javafx.scene.Cursor
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.util.*

class RoomEditor(private val getter: ClassGetter<GameObject>, private vararg val filenames: String): Extension {
    override fun children() = LinkedList<Extension>().apply { add(UIBox()) }

    private lateinit var edit: Room
    private val OFFSET = 40.0
    private val GRID
        get() = 32.0/scale
    private val GRID_P
        get() = PointN(GRID, GRID)

    private var draw_pos = PointN.ZERO

    private var edit_n = 0

    override fun update() {
        clear()
        next()
    }

    private fun next() {
        edit = World.rooms[edit_n]

        play_button.action()
        Platform.update()
        checkForSave()

        checkForEditN()
    }

    private fun checkForEditN() {
        if(keyboard.pressed(KeyCode.CONTROL)) {
            var rp = PointN.ZERO
            if(keyboard.inPressed(KeyCode.UP)) rp -= edit.size.YP/2 + PointN(0, 10)
            if(keyboard.inPressed(KeyCode.DOWN)) rp += edit.size.YP/2 + PointN(0, 10)
            if(keyboard.inPressed(KeyCode.LEFT)) rp -= edit.size.XP/2 + PointN(10, 0)
            if(keyboard.inPressed(KeyCode.RIGHT)) rp += edit.size.XP/2 + PointN(10, 0)

            if(rp != PointN.ZERO) {
                for(index in filenames.indices) {
                    val r = World.rooms[index]
                    if(r.main.into(edit.pos + edit.size/2 + rp)) {
                        edit_n = index
                    }
                }
            }
        } else if(keyboard.pressed(KeyCode.ALT)) {
            if(keyboard.inPressed(KeyCode.UP)) edit_n -= 5
            if(keyboard.inPressed(KeyCode.DOWN)) edit_n += 5
            if(keyboard.inPressed(KeyCode.LEFT)) edit_n--
            if(keyboard.inPressed(KeyCode.RIGHT)) edit_n++
        }

        edit_n = edit_n.coerceIn(filenames.indices)
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
        graphics.fill.rect(PointN.ZERO, CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }


    override fun init() {
        scale = 2
        World.getter = getter

        //todo
        val c = OneRoomController()
        World.init(c, *filenames)
        edit = World.rooms[edit_n]
        c.room = edit

        Platform.whole_draw = true
        //todo
        draw_pos = Platform.options().size/4 - edit.size*PointN(0.5, 0.5)
        UIBox.add(canvasX, play_button, objects_vbox, layers_vbox, info_box)

        /*World.camera = object: Camera {
            override fun drawPOS(): PointN {
                return draw_pos + PointN(1.0, 1000.0) + Platform.CANVAS/2
            }
        }*/
        World.next()
        init0()
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val entries = LinkedList<StringN>()
    private val names = LinkedList<StringN>()
    private val ids = TreeMap<StringN, Int>()
    private val groupsValues = LinkedList<LinkedList<StringN>>()
    private val groupsSelect = LinkedList<Int>()
    private fun init0() {
        entries.addAll(Array(getter.entry_size()) { getter.getEntryName(it) })

        for(id in entries.indices) ids[entries[id]] = id

        val groups_map = TreeMap<StringN, LinkedList<StringN>>()

        fun addNewEntry(name: StringN, entry: StringN) {
            val list = LinkedList<StringN>()
            list.add(entry)
            groups_map[name] = list
        }

        fun getName(entry: StringN): StringN{
            if(!entry.s.contains("#")) {
                return entry
            }
            return StringN(FGUtils.subBefore(entry.s, "#"), entry.n)
        }

        for(entry in entries) {
            val name = getName(entry)
            if(groups_map[name] == null) addNewEntry(name, entry)
            else groups_map[name]?.add(entry)
        }

        for(i in groups_map.keys) {
            groupsValues.add(LinkedList())
        }
        for((id, key) in groups_map.keys.withIndex()){
            val value=groups_map[key]!!
            groupsValues[id].addAll(value)
            names.add(getName(key))
        }

        for(i in groupsValues.indices) {
            groupsSelect.add(0)
        }
    }

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

    private val obj = object: DataGetterRE() {
        override fun get(): DataRE {
            return DataRE(draw_pos, edit, OFFSET, getter, GRID, GRID_P, entries, names, ids, groupsValues, groupsSelect)
        }
    }

    private var objects_vbox = ObjectVBoxRE(obj)

    private var canvasX = object: UICanvas() {
        override val pos: PointN
            get() = PointN.ZERO
        override val size: PointN
            get() = CANVAS
        override val window: RectN
            get() = CANVAS_R

        var draw_bounds = false

        val grid_offset = arrayOf(GRID_P/2, PointN.ZERO, GRID_P.XP/2, GRID_P.YP/2)
        var grid_offset_id = 0

        override fun draw() {
            Platform.global_alpha = 0.2
            World.draw(draw_pos - edit.pos)
            Platform.global_alpha = 1.0

            edit.draw(draw_pos)

            if(draw_bounds) WorldUtils.drawBounds(edit, draw_pos)

            drawLines()

            //graphics.layer = DrawLayer.CAMERA_FOLLOW
            //graphics.stroke.rect(draw_pos, CANVAS, Color.DARKBLUE)

            drawFields()

            Platform.global_alpha = 0.3
            val pp = (mouse.pos()/scale - draw_pos).round(GRID) + draw_pos + grid_offset[grid_offset_id]
            objects_vbox.select_obj.draw(pp)
            if(draw_bounds) WorldUtils.drawBoundsFor(objects_vbox.select_obj, pp)
            Platform.global_alpha = 1.0
        }

        private fun drawFields() {
            graphics.setStroke(1.0)
            World.active_rooms.forEach { r ->
                if(r != edit) graphics.stroke.rect(
                    draw_pos - edit.pos + r.pos,
                    r.size,
                    FGUtils.transparent(Color.WHITE, 0.8))
            }

            graphics.setStroke(3.0)
            graphics.stroke.rect(draw_pos - edit.pos + edit.pos, edit.size, Color.WHITE)
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
            if(keyboard.inPressed(KeyCode.P)) grid_offset_id = MathUtils.mod(grid_offset_id + 1, grid_offset.size)
        }

        private var last_mouse_pos = PointN.ZERO

        override fun update() {

            last_mouse_pos = mouse.pos()
            if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds
        }

        var lastPOS = PointN.ZERO
        private fun checkForAdd() {
            if(mouse_keys.pressed(MouseButton.PRIMARY)) {
                val o = getter.getEntry(objects_vbox.chosen())()
                val pp = (mouse.pos()/scale - draw_pos).round(GRID) + grid_offset[grid_offset_id]
                if(lastPOS == pp) return
                o.stats.POS = pp
                lastPOS = pp
                edit.objects.add(o)
                select_obj = o
            }
        }

        private fun checkForRemove() {
            if(mouse_keys.pressed(MouseButton.SECONDARY)) {
                val sel = getter.getEntry(objects_vbox.chosen())()
                sel.setValues()
                edit.objects.removeIf { o ->
                    o.setValues()
                    (o.stats.POS.lengthTo(mouse.pos()/scale - draw_pos))<=GRID/2 && sel.name == o.name
                }
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
        private fun getL(): List<String> {
            val res = LinkedList<String>()

            res.add("room: ${filenames[edit_n]}")
            res.add("")


            val s = select_obj.toString()
            if(s.indexOf(':') == -1) {
                res.add("object: $s")
                return res
            }

            val name = FGUtils.subBefore(s, ":")
            val args = FGUtils.subAfter(s, ":")
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
            get() = (CANVAS - size).XP + PointN(-OFFSET, 70.0)
        override val size
            get() = PointN(350, 400)/scale
        override val window: RectN
            get() = CANVAS_R
    }
}
