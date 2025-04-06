package com.uzery.fglib.core.room

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.RoomDrawUtils.addObjVis
import com.uzery.fglib.core.room.RoomDrawUtils.drawVisuals
import com.uzery.fglib.core.world.system.room.BasicRoomMoveSystem.getBS
import com.uzery.fglib.core.room.entry.FGRoomEntry
import com.uzery.fglib.utils.BoundsUtils
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.file.FGLibConst
import com.uzery.fglib.utils.data.getter.value.PosValue
import com.uzery.fglib.utils.data.getter.value.SizeValue
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * TODO("doc")
 **/
class Room(val name: String, var pos: PointN, var size: PointN) {
    //todo private
    val objects = ArrayList<GameObject>()
    private val new_objects = ArrayList<GameObject>()
    private val old_objects = HashSet<GameObject>()

    val main
        get() = RectN(pos, size)

    constructor(name: String, pos: PointN, size: PointN, objs: List<GameObject>): this(name, pos, size) {
        objects.addAll(objs)
    }

    fun init() {
        fun initWithFollowers(obj: GameObject) {
            obj.init()
            obj.followers.forEach { initWithFollowers(it) }
        }

        objects.forEach { initWithFollowers(it) }
    }

    fun allowPos(pos: PointN): Boolean {
        return !red_bounds.any { rbs ->
            rbs.bounds.into(pos-rbs.pos)
        }
    }

    fun allowShape(sh: Shape, pos: PointN = PointN.ZERO): Boolean {
        return !red_bounds.any { rbs ->
            BoundsUtils.intoShape(rbs.bounds, sh, pos-rbs.pos)
        }
    }

    fun allowBounds(bs: Bounds, pos: PointN = PointN.ZERO): Boolean {
        return !red_bounds.any { rbs ->
            BoundsUtils.into(rbs.bounds, bs, pos-rbs.pos)
        }
    }

    var all_objs = ArrayList<GameObject>()
        private set
    var red_bounds = ArrayList<PosBounds>()
        private set

    fun next() {
        nextLogics()
        nextTime()
    }

    fun nextLogics() {
        objects.addAll(new_objects)
        new_objects.clear()

        init()

        objects.forEach { it.stats.roomPOS = pos }
        objects.forEach { it.nextLogicsWithFollowers() }

        all_objs = getAllObjects()
        red_bounds = getBS(all_objs)

        fun addFromObj(obj: GameObject) {
            new_objects.addAll(obj.children)
            obj.children.clear()

            obj.followers.forEach { addFromObj(it) }
        }
        objects.forEach { obj -> addFromObj(obj) }

        objects.removeIf { it.dead || it.owner != null }

        objects.removeAll(old_objects)
        old_objects.clear()
    }

    fun nextTime() {
        objects.forEach { it.nextTimeWithFollowers() }
    }

    private fun getAllObjects(): java.util.ArrayList<GameObject> {
        return ArrayList<GameObject>().apply {
            fun addInList(obj: GameObject) {
                add(obj)
                obj.followers.forEach { addInList(it) }
            }
            objects.forEach { addInList(it) }
        }
    }

    fun draw(draw_pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val pos_map = HashMap<Visualiser, PointN>()
        val sort_map = HashMap<Visualiser, PointN>()

        objects.forEach { obj ->
            addObjVis(vis, pos_map, sort_map, obj)
        }
        drawVisuals(draw_pos, vis, pos_map, sort_map)
    }

    fun add(vararg objs: GameObject) = new_objects.addAll(objs)
    fun remove(vararg objs: GameObject) = old_objects.addAll(objs)

    fun add(objs: List<GameObject>) = new_objects.addAll(objs)
    fun remove(objs: List<GameObject>) = old_objects.addAll(objs)

    fun toEntry(): FGRoomEntry {
        val objs = ArrayList<FGEntry>()
        for (o in objects) {
            if (!o.isTemp()) objs.add(o.toEntry())
        }

        return FGRoomEntry(name, PointN(pos), PointN(size), objs)
    }

    override fun toString(): String {
        return buildString {
            append(FGLibConst.FILES_COMMENT)

            append("room: ${PosValue(pos)} ${SizeValue(size)}\n\n")

            for (o in objects) {
                if (!o.isTemp()) append("$o\n")
            }
        }
    }
}
