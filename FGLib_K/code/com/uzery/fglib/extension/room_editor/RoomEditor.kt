package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.obj.visual.Visualiser
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

    val layers = LinkedList<DrawLayer>()
    private val select_layer: Int
        get() = layers_vbox.select

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
        last_edit_room = edit

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
        edit = World.rooms[edit_n]
        c.room = edit

        Platform.whole_draw = true
        //todo
        draw_pos = Platform.options().size/4 - edit.size*PointN(0.5, 0.5)
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

        fun getName(entry: StringN): StringN {
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
        for((id, key) in groups_map.keys.withIndex()) {
            val value = groups_map[key]!!
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
            return DataRE(
                draw_pos,
                edit,
                OFFSET,
                getter,
                GRID,
                GRID_P,
                entries,
                names,
                ids,
                groupsValues,
                groupsSelect,
                draw_bounds)
        }
    }

    private var objects_vbox = ObjectVBoxRE(obj)

    private enum class DRAW_MODE {
        FOCUSED, ALL_ROOMS, OVERVIEW;

        fun nextMode(): DRAW_MODE {
            return DRAW_MODE.values()[MathUtils.mod(this.ordinal + 1, DRAW_MODE.values().size)]
        }
    }

    private var draw_mode = DRAW_MODE.FOCUSED

    private lateinit var last_edit_room: Room
    private var last_edit_n = 0

    var draw_bounds = false

    private var canvasX = object: UICanvas() {
        override val pos: PointN
            get() = PointN.ZERO
        override val size: PointN
            get() = CANVAS
        override val window: RectN
            get() = CANVAS_R

        val grid_offset = arrayOf(GRID_P/2, PointN.ZERO, GRID_P.XP/2, GRID_P.YP/2)
        var grid_offset_id = 0

        var add_size = 0

        override fun draw() {
            fun drawEditRoom(alpha: Double = 1.0) {
                Platform.global_alpha = alpha
                edit.draw(draw_pos)
                Platform.global_alpha = 1.0
            }

            fun drawWorld(alpha: Double = 1.0) {
                Platform.global_alpha = alpha
                World.draw(draw_pos - edit.pos)
                Platform.global_alpha = 1.0
            }

            fun drawSelectLayerVisuals(room: Room, pos: PointN = PointN.ZERO) {
                val visuals = ArrayList<Visualiser>()
                val pos_map = HashMap<Visualiser, PointN>()
                room.objects.forEach { obj ->
                    obj.visuals.forEach { vis ->
                        if(vis.drawLayer() == layers[select_layer - 1]) {
                            pos_map[vis] = obj.stats.POS
                            visuals.add(vis)
                        }
                    }
                }
                Room.drawVisuals(pos + draw_pos, visuals, pos_map)
            }

            fun drawFields() {
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

            fun drawSelectObj(alpha: Double = 1.0) {
                Platform.global_alpha = alpha
                val pp = (mouse.pos()/scale - draw_pos).round(GRID) + draw_pos + grid_offset[grid_offset_id]
                for(i in -add_size..add_size) {
                    for(j in -add_size..add_size) {
                        objects_vbox.select_obj.draw(PointN(i, j)*GRID + pp)
                        if(draw_bounds) WorldUtils.drawBoundsFor(objects_vbox.select_obj, PointN(i, j)*GRID + pp)
                    }
                }

                Platform.global_alpha = 1.0
            }

            fun drawLines() {
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

            fun drawBounds(room: Room, pos: PointN = PointN.ZERO) {
                if(draw_bounds) WorldUtils.drawBounds(room, pos + draw_pos)
            }
            when(draw_mode) {
                DRAW_MODE.FOCUSED -> {
                    drawWorld(0.2)

                    if(select_layer == 0) {
                        drawEditRoom()
                    } else {
                        drawEditRoom(0.5)
                        drawSelectLayerVisuals(edit)
                    }
                    drawLines()
                    drawFields()
                    drawSelectObj(0.3)
                    drawBounds(edit)
                }

                DRAW_MODE.ALL_ROOMS -> {
                    if(select_layer == 0) {
                        drawWorld()
                    } else {
                        drawWorld(0.2)
                        World.rooms.forEach { drawSelectLayerVisuals(it, it.pos - edit.pos) }
                    }
                    drawLines()
                    drawSelectObj(0.3)
                    World.rooms.forEach { drawBounds(it, it.pos - edit.pos) }
                }

                DRAW_MODE.OVERVIEW -> {
                    drawWorld()
                    World.rooms.forEach { drawBounds(it, it.pos - edit.pos) }
                }
            }
        }

        override fun ifActive() {
            fun addLastInfo() {
                last_edit_room = edit
                last_edit_n = edit_n
            }

            fun checkForMove(): Boolean {
                if(!keyboard.pressed(KeyCode.SPACE)) {
                    Program.cursor = Cursor.DEFAULT
                    return false
                }
                if(mouse_keys.anyPressed(*MouseButton.values())) draw_pos += (mouse.pos() - last_mouse_pos)/scale
                Program.cursor = Cursor.CLOSED_HAND
                addLastInfo()

                return true
            }

            fun roomFrom(pos: PointN): Room? {
                return when(draw_mode) {
                    DRAW_MODE.FOCUSED -> edit
                    DRAW_MODE.ALL_ROOMS -> World.rooms.find { it.main.into(pos + edit.pos) }
                    DRAW_MODE.OVERVIEW -> null
                }
            }

            fun onSelectLayer(o: GameObject): Boolean {
                return select_layer == 0 || o.visuals.any { vis -> vis.drawLayer() == layers[select_layer - 1] }
            }

            val mouseRealPos = mouse.pos()/scale - draw_pos

            fun checkForAdd() {
                if(keyboard.inPressed(KeyCode.MINUS)) {
                    add_size--
                }
                if(keyboard.inPressed(KeyCode.EQUALS)) {
                    add_size++
                }
                add_size = add_size.coerceIn(0..10)

                fun add(pos: PointN) {
                    if(mouse_keys.pressed(MouseButton.PRIMARY)) {
                        val o = getter.getEntry(objects_vbox.chosen())()
                        if(select_layer != 0 && !onSelectLayer(o)) return
                        val posWithOffset = pos + mouseRealPos.round(GRID) + grid_offset[grid_offset_id]

                        val room = roomFrom(posWithOffset) ?: return

                        o.stats.POS = posWithOffset + edit.pos - room.pos
                        if(room.objects.any { it.equalsS(o) }) return

                        room.objects.add(o)
                        addLastInfo()

                        select_obj = o
                    }
                }
                for(i in -add_size..add_size) {
                    for(j in -add_size..add_size) {
                        add(PointN(i, j)*GRID)
                    }
                }
            }

            fun checkForRemove() {
                if(mouse_keys.pressed(MouseButton.SECONDARY)) {
                    val sel = getter.getEntry(objects_vbox.chosen())()
                    val room = roomFrom(mouseRealPos) ?: return

                    room.objects.removeIf { o ->
                        val len = (o.stats.POS - edit.pos + room.pos).lengthTo(mouseRealPos.round(GRID) + GRID_P/2)
                        len<=GRID/2*(add_size*2 + 1) && sel.equalsName(o) && onSelectLayer(o)
                    }
                    addLastInfo()

                    if(!room.objects.contains(select_obj)) select_obj = null
                }
            }

            fun checkForEditN() {
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

                addLastInfo()
            }

            checkForEditN()
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
            if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.M)) {
                draw_mode = draw_mode.nextMode()
                if(draw_mode == DRAW_MODE.OVERVIEW) {
                    objects_vbox.hide()
                    layers_vbox.hide()
                    info_box.hide()
                }
                if(draw_mode != DRAW_MODE.OVERVIEW) {
                    objects_vbox.show()
                    layers_vbox.show()
                    info_box.show()
                }
            }
        }
    }

    private var layers_vbox = object: VBox(0, 24) {
        override val full: Int
            get() = layers.size + 1

        override val rows: Int
            get() = layers.size + 1

        override val pos
            get() = (CANVAS - size)*PointN(0.5, 1.0) + PointN(0.0, -OFFSET/2)
        override val window: RectN
            get() = CANVAS_R
        override val sizeOne: PointN
            get() = PointN(78, 56)/scale

        override fun setNames(id: Int): String {
            return ""
        }

        override fun draw(pos: PointN, id: Int) {
            graphics.fill.font = Font.font("TimesNewRoman", FontWeight.EXTRA_BOLD, 22.0)
            val name = when(id) {
                0 -> "ALL"
                else -> layers[id - 1].name
            }
            graphics.fill.textC(pos + PointN(0, 4), name, Color.DARKBLUE)
        }
    }

    private var info_box = object: InfoBox() {
        override val text_draw_offset: Double
            get() = -0.2

        private fun getL(): List<String> {
            val res = LinkedList<String>()

            res.add("room: ${filenames[last_edit_n]}")
            res.add("")
            res.add("pos: ${last_edit_room.pos}")
            res.add("size: ${last_edit_room.size}")
            res.add("objects size: ${last_edit_room.objects.size}")
            for(index in 0 until BoundsBox.SIZE) {
                res.add("bounds[${BoundsBox.name(index)}]: ${WorldUtils.bs_n[last_edit_room]!![index]}")
            }

            res.add("")
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

        override fun update() {
            WorldUtils.nextDebugForRoom(edit)
        }

        override fun text(id: Int) = getL()[id]
        override val text_data_size: Int
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
            get() = PointN(350, 450)/scale
        override val window: RectN
            get() = CANVAS_R
    }
}
