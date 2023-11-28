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
import com.uzery.fglib.extension.room_editor.EditAction
import com.uzery.fglib.extension.room_editor.EditActionCode
import com.uzery.fglib.extension.ui.UICanvas
import com.uzery.fglib.utils.data.file.ConstL
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.Cursor
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import kotlin.math.max
import kotlin.math.min

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
            val visuals_down = ArrayList<Visualiser>()
            val pos_map_down = HashMap<Visualiser, PointN>()
            val sort_map_down = HashMap<Visualiser, PointN>()

            val visuals_up = ArrayList<Visualiser>()
            val pos_map_up = HashMap<Visualiser, PointN>()
            val sort_map_up = HashMap<Visualiser, PointN>()

            room.objects.forEach { obj ->
                obj.visuals.forEach { vis ->
                    if (vis.drawLayer() == data.select_layer) {
                        pos_map_down[vis] = obj.stats.POS
                        sort_map_down[vis] = pos_map_down[vis]!!+obj.stats.sortPOS
                        visuals_down.add(vis)
                    } else if (vis.drawLayer().sort > data.select_layer.sort) {
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
            graphics.setStroke(3.0)
            graphics.stroke.rect(data.draw_pos, data.edit.size, Color.WHITE)
        }

        fun drawChosenObj(alpha: Double = 1.0) {
            if (keyboard.pressed(KeyCode.ALT)) return
            if (!isActive()) return

            Platform.global_alpha = alpha
            val pp = mouseRealPos.roundL(data.GRID)+data.draw_pos+grid_offset[grid_offset_id]
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

            val sizeY = (window.S.Y/view_scale/data.GRID+1).toInt()
            val sizeX = (window.S.X/view_scale/data.GRID+1).toInt()
            for (i in 0..sizeY) {
                graphics.stroke.line(
                    -data.GRID_P+data.draw_pos.mod(data.GRID)
                            +PointN(0.0, i*data.GRID), PointN(window.S.X/view_scale+data.GRID, 0.0), c
                )
            }
            for (i in 0..sizeX) {
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

        fun drawSelectCells() {
            val col = Color(0.1, 0.2, 0.5, 0.4)
            val set = HashSet<PointN>()
            for (pair in data.select_objs) {
                if (!onSelectLayer(pair.first)) continue
                val obj_p = pair.first.stats.POS.roundL(data.GRID)+pair.first.stats.roomPOS-data.edit.pos
                set += obj_p
            }
            for (obj_p in set) {
                graphics.fill.rect(data.draw_pos+obj_p, data.GRID_P, col)
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

            val m_pos = mouseRealPos.roundL(data.GRID)

            val minP = data.draw_pos+
                    if (keyboard.pressed(KeyCode.ALT))
                        PointN.transform(m_pos, start_alt_pos) { o1, o2 -> min(o1, o2) }
                    else m_pos+PointN(1, 1)*(-add_size/2)*data.GRID

            val maxP = data.draw_pos+data.GRID_P+
                    if (keyboard.pressed(KeyCode.ALT))
                        PointN.transform(m_pos, start_alt_pos) { o1, o2 -> max(o1, o2) }
                    else m_pos+PointN(1, 1)*(-add_size/2+add_size)*data.GRID

            val sizeP = maxP-minP

            graphics.stroke.line(minP, sizeP.XP, col)
            graphics.stroke.line(minP, sizeP.YP, col)
            graphics.stroke.line(minP+sizeP.YP, sizeP.XP, col)
            graphics.stroke.line(minP+sizeP.XP, sizeP.YP, col)

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

                if (data.select_layerID == 0) {
                    drawEditRoom()
                } else {
                    drawEditRoom(0.5)
                    drawSelectLayerVisuals(data.edit)
                }
                drawLines()
                drawSelectCells()
                drawCell()
                drawFields()
                drawChosenObj(0.3)
                drawBounds(data.edit)
            }

            DRAW_MODE.ALL_ROOMS -> {
                if (data.select_layerID == 0) {
                    drawWorld()
                } else {
                    drawWorld(0.2)
                    World.rooms.forEach { drawSelectLayerVisuals(it, it.pos-data.edit.pos) } //todo
                }
                drawLines()
                drawSelectCells()
                drawCell()
                drawChosenObj(0.3)
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

    private var start_alt_pos = PointN.ZERO
    private var start_move_pos = PointN.ZERO
    private var already_copy = false
    private var last_selected = HashSet<Pair<GameObject, Room>>()

    private fun onSelectLayer(o: GameObject): Boolean {
        return data.select_layerID == 0 || o.visuals.isEmpty() || o.visuals.any { vis ->
            vis.drawLayer() == data.select_layer
        }
    }

    override fun ifActive() {
        val arrow_pos = PointN(0, 0)
        if (keyboard.inPressed(KeyCode.UP)) arrow_pos.Y--
        if (keyboard.inPressed(KeyCode.DOWN)) arrow_pos.Y++
        if (keyboard.inPressed(KeyCode.LEFT)) arrow_pos.X--
        if (keyboard.inPressed(KeyCode.RIGHT)) arrow_pos.X++
        val grid_pos = arrow_pos*data.GRID

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

        fun checkForAdd() {
            if (keyboard.anyPressed(KeyCode.ALT, KeyCode.CONTROL)) return

            if (!data.redact_field_active) {
                if (keyboard.inPressed(KeyCode.MINUS) || keyboard.timePressed(KeyCode.MINUS)%10 == 9L) {
                    add_size--
                }
                if (keyboard.inPressed(KeyCode.EQUALS) || keyboard.timePressed(KeyCode.EQUALS)%10 == 9L) {
                    add_size++
                }
            }
            add_size = add_size.coerceIn(0..23)

            val listToAdd = ArrayList<Pair<GameObject, Room>>()

            fun add(pos: PointN) {
                if (!mouse.keys.pressed(MouseButton.PRIMARY)) return

                val o = data.getter.getEntry(data.chosen_entry)()
                if (data.select_layerID != 0 && !onSelectLayer(o)) return
                val posWithOffset = pos+mouseRealPos.roundL(data.GRID)+grid_offset[grid_offset_id]

                val room = roomFrom(posWithOffset) ?: return

                o.stats.POS = posWithOffset+data.edit.pos-room.pos
                if (hasEqualObjIn(room, o)) return

                listToAdd.add(Pair(o, room))
                addLastInfo()

                data.select_objs.clear()
                data.select_objs.add(Pair(o, room))
            }

            for (i in -add_size/2..(add_size+1)/2) {
                for (j in -add_size/2..(add_size+1)/2) {
                    add(PointN(i, j)*data.GRID)
                }
            }
            addObjs(listToAdd, true)
        }

        fun checkForRemove() {
            if (keyboard.inPressed(KeyCode.DELETE)) {
                removeObjs(data.select_objs.toList(), true)
                data.select_objs.clear()
            }

            if (!mouse.keys.pressed(MouseButton.SECONDARY)) return
            if (keyboard.anyPressed(KeyCode.ALT, KeyCode.CONTROL)) return

            val sel = data.getter.getEntry(data.chosen_entry)()
            val room = roomFrom(mouseRealPos) ?: return

            val listToRemove = ArrayList<GameObject>()
            room.objects.forEach { o ->
                val pos1 = (o.stats.POS-data.edit.pos+room.pos).roundL(data.GRID)
                val added = if (add_size%2 == 0) PointN.ZERO else data.GRID_P/2
                val pos2 = mouseRealPos.roundL(data.GRID)+added
                val len = max(pos1.XP.lengthTo(pos2.XP), pos1.YP.lengthTo(pos2.YP))

                if (len <= data.GRID/2*(add_size+1) && sel.equalsName(o) && onSelectLayer(o)) {
                    listToRemove.add(o)
                }
            }
            removeObjs(listToRemove, room, true)

            addLastInfo()
        }

        val anyMBPressed = mouse.keys.anyPressed(MouseButton.PRIMARY, MouseButton.SECONDARY)
        fun checkForSelect() {
            val m_pos = mouseRealPos.roundL(data.GRID)

            if (keyboard.pressed(KeyCode.ALT) && !anyMBPressed) {
                start_alt_pos = m_pos
            }

            if (!anyMBPressed) return
            if (!keyboard.pressed(KeyCode.ALT)) return

            val room = roomFrom(mouseRealPos) ?: return

            val minP = PointN.transform(m_pos, start_alt_pos) { o1, o2 -> min(o1, o2) }
            val maxP = PointN.transform(m_pos, start_alt_pos) { o1, o2 -> max(o1, o2) }

            val rect = RectN.LR(minP, data.GRID_P+maxP)

            if (!keyboard.pressed(KeyCode.CONTROL)) data.select_objs.clear()
            room.objects.forEach { o ->
                val o_pos = (o.stats.POS-data.edit.pos+room.pos).roundL(data.GRID)+data.GRID_P/2

                if (rect.into(o_pos) && onSelectLayer(o) && (o.visuals.isNotEmpty() || data.select_layerID == 0)) {
                    val pair = Pair(o, room)
                    if (mouse.keys.pressed(MouseButton.PRIMARY)) {
                        data.select_objs.add(pair)
                    } else {
                        data.select_objs.remove(pair)
                    }
                }
            }
            addLastInfo()
        }

        fun checkForMove() {
            if (keyboard.pressed(KeyCode.R)) return
            if (keyboard.pressed(KeyCode.SPACE)) return

            if (keyboard.pressed(KeyCode.CONTROL) && !mouse.keys.anyPressed(
                    MouseButton.PRIMARY,
                    MouseButton.SECONDARY
                )
            ) {
                start_move_pos = mouseRealPos.roundL(data.GRID)
                already_copy = false
            }

            if (keyboard.pressed(KeyCode.CONTROL) && mouse.keys.pressed(MouseButton.PRIMARY) && !already_copy) {
                val listToAdd = ArrayList<Pair<GameObject, Room>>()
                data.select_objs.filter { it.first.name != "temp" }.forEach { pair ->
                    listToAdd.add(Pair(data.getter[pair.first.toString()], pair.second))
                }
                addObjs(listToAdd, true)
                already_copy = true
            }



            if (keyboard.pressed(KeyCode.CONTROL) && anyMBPressed) {
                moveObjs(data.select_objs.toList(), mouseRealPos.roundL(data.GRID)-start_move_pos, true)
                start_move_pos = mouseRealPos.roundL(data.GRID)
            }

            if (grid_pos.length() < 0.1) return

            when {
                keyboard.allPressed(KeyCode.CONTROL, KeyCode.SHIFT) -> {
                    World.rooms.forEach {
                        moveObjs(it.objects, it, grid_pos, true)
                    }
                }

                keyboard.pressed(KeyCode.CONTROL) -> {
                    moveObjs(data.edit.objects, data.edit, grid_pos, true)
                }

                !keyboard.anyPressed(KeyCode.ALT, KeyCode.SHIFT, KeyCode.CONTROL) -> {
                    moveObjs(data.select_objs.toList(), grid_pos, true)
                }
            }
        }

        fun changeEditRoom() {
            if (!keyboard.pressed(KeyCode.R)) return
            if (grid_pos.length() < 0.1) return

            when {
                keyboard.pressed(KeyCode.CONTROL) -> {
                    data.edit.size += grid_pos
                }

                else -> {
                    data.edit.pos += grid_pos
                }
            }
        }

        fun checkForEditN() {
            val wasd_pos = PointN(0, 0)
            if (keyboard.inPressed(KeyCode.W)) wasd_pos.Y--
            if (keyboard.inPressed(KeyCode.S)) wasd_pos.Y++
            if (keyboard.inPressed(KeyCode.A)) wasd_pos.X--
            if (keyboard.inPressed(KeyCode.D)) wasd_pos.X++

            if (wasd_pos.length() < 0.1 && !keyboard.pressed(KeyCode.X)) return

            when {
                keyboard.pressed(KeyCode.X) -> {
                    if (keyboard.inPressed(KeyCode.UP)) data.edit_n -= 5
                    if (keyboard.inPressed(KeyCode.DOWN)) data.edit_n += 5
                    if (keyboard.inPressed(KeyCode.LEFT)) data.edit_n--
                    if (keyboard.inPressed(KeyCode.RIGHT)) data.edit_n++
                }

                else -> {
                    val rp = (data.edit.size/2+PointN(10, 10))*wasd_pos

                    if (rp != PointN.ZERO) {
                        for ((index, room) in World.rooms.withIndex()) {
                            if (room.main.into(data.edit.pos+data.edit.size/2+rp)) {
                                data.edit_n = index
                            }
                        }
                    }
                }
            }


            data.edit_n = data.edit_n.coerceIn(data.filenames.indices)

            addLastInfo()
        }

        fun removeUnselectCopies() {
            val listToRemove = ArrayList<Pair<GameObject, Room>>()
            last_selected.forEach { pair ->
                if (pair !in data.select_objs) {
                    if (hasEqualObjIn(pair.second, pair.first)) {
                        listToRemove.add(pair)
                    }
                }
            }
            removeObjs(listToRemove, true)

            last_selected = HashSet()
            data.select_objs.forEach { pair -> last_selected += pair }
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


        if (keyboard.pressed(KeyCode.SPACE)) return

        checkForMove()
        changeEditRoom()
        checkForEditN()
        checkForAdd()
        checkForRemove()
        checkForSelect()
        removeUnselectCopies()
        if (keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.Z)) undo()

        if (keyboard.inPressed(KeyCode.P)) grid_offset_id = MathUtils.mod(grid_offset_id+1, grid_offset.size)
    }

    private fun hasEqualObjIn(room: Room, obj: GameObject): Boolean {
        return room.objects.any {
            areEqualButNotSame(it, obj)
        }
    }
    private fun areEqualButNotSame(obj1: GameObject, obj2: GameObject): Boolean {
        return obj1 != obj2 && obj1.equalsS(obj2) && (obj1.name != "temp" || obj1.stats.POS == obj2.stats.POS)
    }

    private val history = ArrayList<EditAction>()

    private fun undo() {
        if (history.isEmpty()) return
        val action = history.removeLast()
        when (action.code) {
            EditActionCode.ADD -> removeObjs(action.list, false)
            EditActionCode.REMOVE -> addObjs(action.list, false)
            EditActionCode.MOVE -> moveObjs(action.list, -action.pos, false)
        }
        data.select_objs.clear()
    }

    private fun addObjs(list: List<Pair<GameObject, Room>>, assign: Boolean) {
        if (list.isEmpty()) return
        list.forEach { it.second.objects.add(it.first) }
        if (!assign) return
        history.add(EditAction(EditActionCode.ADD, list))
    }

    private fun removeObjs(list: List<Pair<GameObject, Room>>, assign: Boolean) {
        if (list.isEmpty()) return
        list.forEach { it.second.objects.remove(it.first) }
        data.select_objs.removeIf { obj -> list.any { it.first == obj.first } }
        if (!assign) return
        history.add(EditAction(EditActionCode.REMOVE, list))
    }

    private fun moveObjs(list: List<Pair<GameObject, Room>>, move_pos: PointN, assign: Boolean) {
        if (list.isEmpty()) return
        if (move_pos.length() < ConstL.LITTLE) return

        list.forEach { it.first.stats.POS += move_pos }
        if (!assign) return
        history.add(EditAction(EditActionCode.MOVE, list, move_pos))
    }

    private fun removeObjs(list: List<GameObject>, room: Room, assign: Boolean) {
        removeObjs(toPairList(list, room), assign)
    }

    private fun moveObjs(list: List<GameObject>, room: Room, move_pos: PointN, assign: Boolean) {
        moveObjs(toPairList(list, room), move_pos, assign)
    }

    private fun toPairList(list: List<GameObject>, room: Room): ArrayList<Pair<GameObject, Room>> {
        val res = ArrayList<Pair<GameObject, Room>>()
        list.forEach {
            res.add(Pair(it, room))
        }
        return res
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
