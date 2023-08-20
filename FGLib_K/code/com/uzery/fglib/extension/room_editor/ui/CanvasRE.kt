package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.core.program.Platform.mouse_keys
import com.uzery.fglib.core.program.Program
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.UICanvas
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.Cursor
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color

class CanvasRE(private val data: DataRE): UICanvas() {
    private enum class DRAW_MODE {
        FOCUSED, ALL_ROOMS, OVERVIEW;

        fun nextMode(): DRAW_MODE {
            return DRAW_MODE.values()[MathUtils.mod(this.ordinal+1, DRAW_MODE.values().size)]
        }
    }

    private var draw_mode = DRAW_MODE.FOCUSED


    override val pos: PointN
        get() = PointN.ZERO
    override val size: PointN
        get() = Platform.CANVAS
    override val window: RectN
        get() = Platform.CANVAS_R

    private val grid_offset = arrayOf(data.GRID_P/2, PointN.ZERO, data.GRID_P.XP/2, data.GRID_P.YP/2)
    private var grid_offset_id = 0

    private var add_size = 0

    private var draw_lines = true
    private var draw_layers = true

    override fun draw() {
        fun drawEditRoom(alpha: Double = 1.0) {
            Platform.global_alpha = alpha
            data.edit.draw(data.draw_pos)
            Platform.global_alpha = 1.0
        }

        fun drawWorld(alpha: Double = 1.0) {
            Platform.global_alpha = alpha
            World.draw(data.draw_pos-data.edit.pos)
            Platform.global_alpha = 1.0
        }

        fun drawSelectLayerVisuals(room: Room, pos: PointN = PointN.ZERO) {
            val visuals = ArrayList<Visualiser>()
            val pos_map = HashMap<Visualiser, PointN>()

            val visuals_up = ArrayList<Visualiser>()
            val pos_map_up = HashMap<Visualiser, PointN>()
            room.objects.forEach { obj ->
                obj.visuals.forEach { vis ->
                    if (vis.drawLayer() == data.layers[data.select_layer-1]) {
                        pos_map[vis] = obj.stats.POS
                        visuals.add(vis)
                    } else if (vis.drawLayer().sort > data.layers[data.select_layer-1].sort) {
                        pos_map_up[vis] = obj.stats.POS
                        visuals_up.add(vis)
                    }
                }
            }
            Room.drawVisuals(pos+data.draw_pos, visuals, pos_map)

            if (!draw_layers) return

            Platform.global_alpha = 0.2
            Room.drawVisuals(pos+data.draw_pos, visuals_up, pos_map_up)
            Platform.global_alpha = 1.0
        }

        fun drawFields() {
            /*graphics.setStroke(1.0)
            World.active_rooms.forEach { r ->
                if (r != data.edit) graphics.stroke.rect(
                    data.draw_pos-data.edit.pos+r.pos,
                    r.size,
                    FGUtils.transparent(Color.WHITE, 0.8)
                )
            }*/

            graphics.setStroke(3.0)
            graphics.stroke.rect(data.draw_pos, data.edit.size, Color.WHITE)
        }

        fun drawSelectObj(alpha: Double = 1.0) {
            Platform.global_alpha = alpha
            val pp =
                (mouse.pos()/Platform.scale-data.draw_pos).round(data.GRID)+data.draw_pos+grid_offset[grid_offset_id]
            for (i in -add_size..add_size) {
                for (j in -add_size..add_size) {
                    val obj = data.chosen_obj ?: continue
                    obj.draw(PointN(i, j)*data.GRID+pp)
                    if (data.draw_bounds) WorldUtils.drawBoundsFor(obj, PointN(i, j)*data.GRID+pp)
                }
            }

            Platform.global_alpha = 1.0
        }

        fun drawLines() {
            if (!draw_lines) return

            val c = FGUtils.transparent(Color.WHITE, 0.1)
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.setStroke(1.0)
            Program.gc.setLineDashes(5.0, 5.0)
            Program.gc.lineDashOffset = 1.0

            for (i in 0..(window.S.Y/data.GRID+1).toInt()) {
                graphics.stroke.line(
                    -data.GRID_P+data.draw_pos.mod(data.GRID)
                            +PointN(0.0, i*data.GRID), PointN(window.S.X+data.GRID, 0.0), c
                )
            }
            for (i in 0..(window.S.X/data.GRID+1).toInt()) {
                graphics.stroke.line(
                    -data.GRID_P+data.draw_pos.mod(data.GRID)
                            +PointN(i*data.GRID, 0.0), PointN(0.0, window.S.Y+data.GRID), c
                )
            }
            Program.gc.setLineDashes()
            Program.gc.lineDashOffset = 0.0

            graphics.setStroke(2.0)
            for (room in World.rooms) {
                graphics.stroke.draw(data.draw_pos-data.edit.pos, room.main, FGUtils.transparent(Color.WHITE, 0.7))
            }

        }

        fun drawBounds(room: Room, pos: PointN = PointN.ZERO) {
            if (data.draw_bounds) WorldUtils.drawBounds(room, pos+data.draw_pos)
        }
        when (draw_mode) {
            DRAW_MODE.FOCUSED -> {
                drawWorld(0.2)

                if (data.select_layer == 0) {
                    drawEditRoom()
                } else {
                    drawEditRoom(0.5)
                    drawSelectLayerVisuals(data.edit)
                }
                drawLines()
                drawFields()
                drawSelectObj(0.3)
                drawBounds(data.edit)
            }

            DRAW_MODE.ALL_ROOMS -> {
                if (data.select_layer == 0) {
                    drawWorld()
                } else {
                    drawWorld(0.2)
                    World.rooms.forEach { drawSelectLayerVisuals(it, it.pos-data.edit.pos) } //todo
                }
                drawLines()
                drawSelectObj(0.3)
                World.rooms.forEach { drawBounds(it, it.pos-data.edit.pos) }
            }

            DRAW_MODE.OVERVIEW -> {
                drawWorld()
                World.rooms.forEach { drawBounds(it, it.pos-data.edit.pos) }
            }
        }
    }

    override fun ifActive() {
        fun addLastInfo() {
            data.last_edit_room = data.edit
            data.last_edit_n = data.edit_n
        }

        fun roomFrom(pos: PointN): Room? {
            return when (draw_mode) {
                DRAW_MODE.FOCUSED -> data.edit
                DRAW_MODE.ALL_ROOMS -> World.rooms.find { it.main.into(pos+data.edit.pos) }
                DRAW_MODE.OVERVIEW -> null
            }
        }

        fun onSelectLayer(o: GameObject): Boolean {
            return data.select_layer == 0 || o.visuals.isEmpty() || o.visuals.any { vis -> vis.drawLayer() == data.layers[data.select_layer-1] }
        }

        val mouseRealPos = mouse.pos()/Platform.scale-data.draw_pos

        fun checkForAdd() {
            if (keyboard.inPressed(KeyCode.MINUS)) {
                add_size--
            }
            if (keyboard.inPressed(KeyCode.EQUALS)) {
                add_size++
            }
            add_size = add_size.coerceIn(0..10)

            fun add(pos: PointN) {
                if (mouse_keys.pressed(MouseButton.PRIMARY)) {
                    val o = data.getter.getEntry(data.chosen_entry)()
                    if (data.select_layer != 0 && !onSelectLayer(o)) return
                    val posWithOffset = pos+mouseRealPos.round(data.GRID)+grid_offset[grid_offset_id]

                    val room = roomFrom(posWithOffset) ?: return

                    o.stats.POS = posWithOffset+data.edit.pos-room.pos
                    if (room.objects.any { it.equalsS(o) }) return

                    room.objects.add(o)
                    addLastInfo()

                    data.select_obj = o
                }
            }
            for (i in -add_size..add_size) {
                for (j in -add_size..add_size) {
                    add(PointN(i, j)*data.GRID)
                }
            }
        }

        fun checkForRemove() {
            if (mouse_keys.pressed(MouseButton.SECONDARY)) {
                val sel = data.getter.getEntry(data.chosen_entry)()
                val room = roomFrom(mouseRealPos) ?: return

                room.objects.removeIf { o ->
                    val len =
                        (o.stats.POS-data.edit.pos+room.pos).lengthTo(mouseRealPos.round(data.GRID)+data.GRID_P/2)
                    len <= data.GRID/2*(add_size*2+1) && sel.equalsName(o) && onSelectLayer(o)
                }
                addLastInfo()

                if (!room.objects.contains(data.select_obj)) data.select_obj = null
            }
        }

        fun checkForEditN() {
            if (keyboard.pressed(KeyCode.CONTROL)) {
                var rp = PointN.ZERO
                if (keyboard.inPressed(KeyCode.UP)) rp -= data.edit.size.YP/2+PointN(0, 10)
                if (keyboard.inPressed(KeyCode.DOWN)) rp += data.edit.size.YP/2+PointN(0, 10)
                if (keyboard.inPressed(KeyCode.LEFT)) rp -= data.edit.size.XP/2+PointN(10, 0)
                if (keyboard.inPressed(KeyCode.RIGHT)) rp += data.edit.size.XP/2+PointN(10, 0)

                if (rp != PointN.ZERO) {
                    for (index in data.filenames.indices) {
                        val r = World.rooms[index]
                        if (r.main.into(data.edit.pos+data.edit.size/2+rp)) {
                            data.edit_n = index
                        }
                    }
                }
            } else if (keyboard.pressed(KeyCode.ALT)) {
                if (keyboard.inPressed(KeyCode.UP)) data.edit_n -= 5
                if (keyboard.inPressed(KeyCode.DOWN)) data.edit_n += 5
                if (keyboard.inPressed(KeyCode.LEFT)) data.edit_n--
                if (keyboard.inPressed(KeyCode.RIGHT)) data.edit_n++
            }

            data.edit_n = data.edit_n.coerceIn(data.filenames.indices)

            addLastInfo()
        }

        checkForEditN()
        if (keyboard.pressed(KeyCode.SPACE)) return

        checkForAdd()
        checkForRemove()
        if (keyboard.inPressed(KeyCode.P)) grid_offset_id = MathUtils.mod(grid_offset_id+1, grid_offset.size)
    }

    private var last_mouse_pos = PointN.ZERO

    override fun update() {
        if (keyboard.inPressed(KeyCode.F3)) draw_lines = !draw_lines
        if (keyboard.inPressed(KeyCode.F4)) draw_layers = !draw_layers
        if (keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) data.draw_bounds =
            !data.draw_bounds
        if (keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.M)) {
            draw_mode = draw_mode.nextMode()
            data.hide_ui = draw_mode == DRAW_MODE.OVERVIEW
        }
        fun checkForMove(): Boolean {
            if (!keyboard.pressed(KeyCode.SPACE)) {
                Program.cursor = Cursor.DEFAULT
                return false
            }
            if (mouse_keys.anyPressed(*MouseButton.values())) data.draw_pos += (mouse.pos()-last_mouse_pos)/Platform.scale
            Program.cursor = Cursor.CLOSED_HAND

            return true
        }
        checkForMove()

        last_mouse_pos = mouse.pos()
    }
}