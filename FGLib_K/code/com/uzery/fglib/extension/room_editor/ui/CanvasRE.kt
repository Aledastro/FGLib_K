package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.core.program.Program
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.UICanvas
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.Cursor
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import kotlin.math.max

class CanvasRE(private val data: DataRE): UICanvas() {
    private enum class DRAW_MODE {
        FOCUSED, ALL_ROOMS, OVERVIEW;

        fun nextMode(): DRAW_MODE {
            return DRAW_MODE.values()[MathUtils.mod(this.ordinal+1, DRAW_MODE.values().size)]
        }
    }

    private var draw_mode = DRAW_MODE.FOCUSED

    private var view_scale_sizes = arrayOf(
        0.05, 0.1, 0.15, 0.2, 0.25, 0.3, 0.5, 0.75,
        1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0
    )
    private var view_scaleID = view_scale_sizes.indexOf(1.0)
    private var view_scale = 1.0


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
            data.edit.draw(data.draw_pos-data.edit.pos)
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
            val sort_map = HashMap<Visualiser, PointN>()

            val visuals_down = ArrayList<Visualiser>()
            val pos_map_down = HashMap<Visualiser, PointN>()
            val sort_map_down = HashMap<Visualiser, PointN>()

            val visuals_up = ArrayList<Visualiser>()
            val pos_map_up = HashMap<Visualiser, PointN>()
            val sort_map_up = HashMap<Visualiser, PointN>()

            /*room.objects.forEach { obj ->
                Room.addObjVis(visuals, pos_map, sort_map, obj)
            }

            visuals.forEach { vis ->
                if (vis.drawLayer() == data.layers[data.select_layer-1]) {
                    visuals_down.add(vis)
                    pos_map_down[vis] = pos+pos_map[vis]!!
                    sort_map_down[vis] = pos+sort_map[vis]!!
                } else if (vis.drawLayer().sort > data.layers[data.select_layer-1].sort) {
                    visuals_up.add(vis)
                    pos_map_up[vis] = pos+pos_map[vis]!!
                    sort_map_up[vis] = pos+sort_map[vis]!!
                }
            }*/

            room.objects.forEach { obj ->
                obj.visuals.forEach { vis ->
                    if (vis.drawLayer() == data.layers[data.select_layer-1]) {
                        pos_map_down[vis] = obj.stats.POS
                        sort_map_down[vis] = pos_map_down[vis]!!+obj.stats.sortPOS
                        visuals_down.add(vis)
                    } else if (vis.drawLayer().sort > data.layers[data.select_layer-1].sort) {
                        pos_map_up[vis] = obj.stats.POS
                        sort_map_up[vis] = pos_map_up[vis]!!+obj.stats.sortPOS
                        visuals_up.add(vis)
                    }
                }
            }

            Room.drawVisuals(pos+data.draw_pos, visuals_down, pos_map_down, sort_map_down)

            if (!draw_layers) return

            Platform.global_alpha = 0.2
            Room.drawVisuals(pos+data.draw_pos, visuals_up, pos_map_up, sort_map_up)
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
            if (keyboard.pressed(KeyCode.ALT)) return
            if (!isActive()) return

            Platform.global_alpha = alpha
            val pp =
                (mouse.pos/view_scale-data.draw_pos).roundL(data.GRID)+data.draw_pos+grid_offset[grid_offset_id]
            for (i in -add_size/2..(add_size+1)/2) {
                for (j in -add_size/2..(add_size+1)/2) {
                    val obj = data.chosen_obj ?: continue
                    obj.draw(PointN(i, j)*data.GRID+pp)
                    if (data.draw_bounds) WorldUtils.drawBoundsFor(obj, PointN(i, j)*data.GRID+pp)
                }
            }

            Platform.global_alpha = 1.0
        }

        fun drawLines() {
            if (!draw_lines) return
            if (view_scale < 0.5) return

            val c = Color(0.0, 0.0, 0.0, 0.1)
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.setStroke(0.8)
            Program.gc.setLineDashes(5.0*view_scale, 5.0*view_scale) //todo into graphics
            Program.gc.lineDashOffset = 1.0

            for (i in 0..(window.S.Y/view_scale/data.GRID+1).toInt()) {
                graphics.stroke.line(
                    -data.GRID_P+data.draw_pos.mod(data.GRID)
                            +PointN(0.0, i*data.GRID), PointN(window.S.X/view_scale+data.GRID, 0.0), c
                )
            }
            for (i in 0..(window.S.X/view_scale/data.GRID+1).toInt()) {
                graphics.stroke.line(
                    -data.GRID_P+data.draw_pos.mod(data.GRID)
                            +PointN(i*data.GRID, 0.0), PointN(0.0, window.S.Y/view_scale+data.GRID), c
                )
            }

            Program.gc.setLineDashes()
            Program.gc.lineDashOffset = 0.0

            graphics.setStroke(2.0)
            for (room in World.rooms) {
                graphics.stroke.draw(data.draw_pos-data.edit.pos, room.main, FGUtils.transparent(Color.WHITE, 0.4))
            }

        }

        fun drawCell() {
            if (!keyboard.pressed(KeyCode.ALT) && !draw_lines) return
            if (!isActive()) return

            graphics.setStroke(1.2)
            Program.gc.setLineDashes(5.0*view_scale, 5.0*view_scale) //todo into graphics
            Program.gc.lineDashOffset = 1.0

            val col = if (keyboard.pressed(KeyCode.ALT)) {
                FGUtils.transparent(Color.GOLD, 0.9)
            } else {
                FGUtils.transparent(Color.CYAN.interpolate(Color.WHITE, 0.5), 0.8)
            }
            val cell_size = if (keyboard.pressed(KeyCode.ALT)) 0 else add_size

            val cell_off = PointN(-cell_size/2, -cell_size/2)*data.GRID

            val pos = ((mouse.pos)/view_scale-
                    (data.draw_pos).mod(data.GRID)).roundL(data.GRID)+(data.draw_pos).mod(data.GRID)+cell_off

            graphics.stroke.line(pos, PointN(cell_size+1, 0)*data.GRID, col)
            graphics.stroke.line(pos, PointN(0, cell_size+1)*data.GRID, col)
            graphics.stroke.line(pos+PointN(cell_size+1, 0)*data.GRID, PointN(0, cell_size+1)*data.GRID, col)
            graphics.stroke.line(pos+PointN(0, cell_size+1)*data.GRID, PointN(cell_size+1, 0)*data.GRID, col)

            Program.gc.setLineDashes()
            Program.gc.lineDashOffset = 0.0
        }

        fun drawBounds(room: Room, pos: PointN = PointN.ZERO) {
            if (data.draw_bounds) WorldUtils.drawBounds(room, pos+data.draw_pos)
        }


        Platform.global_view_scale = view_scale
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
                drawCell()
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
                drawCell()
                drawSelectObj(0.3)
                World.rooms.forEach { drawBounds(it, it.pos-data.edit.pos) }
            }

            DRAW_MODE.OVERVIEW -> {
                drawWorld()
                World.rooms.forEach { drawBounds(it, it.pos-data.edit.pos) }
            }
        }
        Platform.global_view_scale = 1.0
    }

    private val mouseRealPos
        get() = mouse.pos/view_scale-data.draw_pos

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

        fun checkForAdd() {
            if (keyboard.inPressed(KeyCode.MINUS) || keyboard.timePressed(KeyCode.MINUS)%10 == 9L) {
                add_size--
            }
            if (keyboard.inPressed(KeyCode.EQUALS) || keyboard.timePressed(KeyCode.EQUALS)%10 == 9L) {
                add_size++
            }
            add_size = add_size.coerceIn(0..23)

            fun add(pos: PointN) {
                if (!mouse.keys.pressed(MouseButton.PRIMARY)) return
                if (keyboard.pressed(KeyCode.ALT)) return

                val o = data.getter.getEntry(data.chosen_entry)()
                if (data.select_layer != 0 && !onSelectLayer(o)) return
                val posWithOffset = pos+mouseRealPos.roundL(data.GRID)+grid_offset[grid_offset_id]

                val room = roomFrom(posWithOffset) ?: return

                o.stats.POS = posWithOffset+data.edit.pos-room.pos
                if (room.objects.any { it.equalsS(o) && (it.name != "temp" || it.stats.POS == o.stats.POS) }) return

                room.objects.add(o)
                addLastInfo()

                data.select_objs.clear()
                data.select_objs.add(Pair(o, room))
            }
            for (i in -add_size/2..(add_size+1)/2) {
                for (j in -add_size/2..(add_size+1)/2) {
                    add(PointN(i, j)*data.GRID)
                }
            }
        }

        fun checkForRemove() {
            if (!mouse.keys.pressed(MouseButton.SECONDARY)) return
            if (keyboard.pressed(KeyCode.ALT)) return

            val sel = data.getter.getEntry(data.chosen_entry)()
            val room = roomFrom(mouseRealPos) ?: return

            val list = ArrayList<GameObject>()
            room.objects.forEach { o ->
                val pos1 = (o.stats.POS-data.edit.pos+room.pos).roundL(data.GRID)
                val added = if (add_size%2 == 0) PointN.ZERO else data.GRID_P/2
                val pos2 = (mouseRealPos.roundL(data.GRID)+data.GRID_P/2).roundL(data.GRID)+added
                val len = max(pos1.XP.lengthTo(pos2.XP), pos1.YP.lengthTo(pos2.YP))

                if (len <= data.GRID/2*(add_size+1) && sel.equalsName(o) && onSelectLayer(o)) {
                    list.add(o)
                }
            }
            data.select_objs.removeIf { it.first in list }
            room.objects.removeAll(list)

            addLastInfo()
        }

        fun checkForSelect() {
            if (!mouse.keys.pressed(MouseButton.PRIMARY)) return
            if (!keyboard.pressed(KeyCode.ALT)) return

            val room = roomFrom(mouseRealPos) ?: return

            data.select_objs.clear()
            room.objects.forEach { o ->
                val pos1 = (o.stats.POS-data.edit.pos+room.pos).roundL(data.GRID)
                val pos2 = (mouseRealPos.roundL(data.GRID)+data.GRID_P/2).roundL(data.GRID)
                val len = max(pos1.XP.lengthTo(pos2.XP), pos1.YP.lengthTo(pos2.YP))

                if (len <= data.GRID/2 && onSelectLayer(o)) data.select_objs.add(Pair(o, room))
            }
            addLastInfo()
        }

        fun checkForEditN() {
            val arrows_pos = PointN(0, 0)
            if (keyboard.inPressed(KeyCode.UP)) arrows_pos.Y--
            if (keyboard.inPressed(KeyCode.DOWN)) arrows_pos.Y++
            if (keyboard.inPressed(KeyCode.LEFT)) arrows_pos.X--
            if (keyboard.inPressed(KeyCode.RIGHT)) arrows_pos.X++

            if(arrows_pos.length() < 0.1) return

            when{
                keyboard.allPressed(KeyCode.CONTROL, KeyCode.SHIFT) -> {
                    World.rooms.forEach { r -> r.objects.forEach { it.stats.POS += arrows_pos*data.GRID } }
                }
                keyboard.pressed(KeyCode.SHIFT) ->{
                    data.edit.objects.forEach { it.stats.POS += arrows_pos*data.GRID }
                }
                keyboard.pressed(KeyCode.CONTROL) -> {
                    val rp = (data.edit.size/2 + PointN(10, 10))*arrows_pos

                    if (rp != PointN.ZERO) {
                        for ((index, room) in World.rooms.withIndex()) {
                            if (room.main.into(data.edit.pos+data.edit.size/2+rp)) {
                                data.edit_n = index
                            }
                        }
                    }
                }
                keyboard.pressed(KeyCode.ALT) -> {
                    if (keyboard.inPressed(KeyCode.UP)) data.edit_n -= 5
                    if (keyboard.inPressed(KeyCode.DOWN)) data.edit_n += 5
                    if (keyboard.inPressed(KeyCode.LEFT)) data.edit_n--
                    if (keyboard.inPressed(KeyCode.RIGHT)) data.edit_n++
                }
            }

            data.edit_n = data.edit_n.coerceIn(data.filenames.indices)

            addLastInfo()
        }

        val old_view_scaleID = view_scaleID
        when (mouse.scroll) {
            -1 -> view_scaleID--
            1 -> view_scaleID++
        }
        view_scaleID = view_scaleID.coerceIn(view_scale_sizes.indices)

        view_scale = view_scale_sizes[view_scaleID]
        val old_view_scale = view_scale_sizes[old_view_scaleID]
        if (view_scaleID != old_view_scaleID) {
            data.draw_pos += (mouse.pos)*(1-view_scale/old_view_scale)/view_scale
        }


        checkForEditN()
        if (keyboard.pressed(KeyCode.SPACE)) return

        checkForAdd()
        checkForRemove()
        checkForSelect()
        if (keyboard.inPressed(KeyCode.P)) grid_offset_id = MathUtils.mod(grid_offset_id+1, grid_offset.size)
    }

    private var last_mouse_pos = PointN.ZERO

    override fun update() {
        Platform.cursor = Cursor.DEFAULT

        if (keyboard.inPressed(KeyCode.F3)) draw_lines = !draw_lines
        if (keyboard.inPressed(KeyCode.F4)) draw_layers = !draw_layers
        if (keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) data.draw_bounds =
            !data.draw_bounds
        if (keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.M)) {
            draw_mode = draw_mode.nextMode()
            data.hide_ui = draw_mode == DRAW_MODE.OVERVIEW
        }
        fun checkForMove(): Boolean {
            if (!keyboard.pressed(KeyCode.SPACE)) return false

            Platform.cursor = Cursor.OPEN_HAND
            if (mouse.keys.anyPressed(*MouseButton.values())) data.draw_pos += (mouse.pos-last_mouse_pos)/view_scale
            Platform.cursor = Cursor.CLOSED_HAND

            return true
        }

        if (keyboard.pressed(KeyCode.ALT)) Platform.cursor = Cursor.CROSSHAIR

        checkForMove()

        World.rooms.forEach { room -> room.objects.forEach { it.stats.roomPOS = room.pos } }

        last_mouse_pos = mouse.pos
    }
}
