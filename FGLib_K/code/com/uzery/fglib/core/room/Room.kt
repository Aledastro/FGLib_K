package com.uzery.fglib.core.room

import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.room.RoomActivateLogics.nextActivate
import com.uzery.fglib.core.room.RoomMoveLogics.nextMoveOld
import com.uzery.fglib.core.room.RoomUtils.addObjVis
import com.uzery.fglib.core.room.RoomUtils.drawVisuals
import com.uzery.fglib.core.room.entry.FGRoomEntry
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * TODO("doc")
 **/
class Room(var pos: PointN, var size: PointN) {
    //todo private
    val objects = ArrayList<GameObject>()
    private val new_objects = ArrayList<GameObject>()
    private val old_objects = HashSet<GameObject>()

    val main
        get() = RectN(pos, size)

    constructor(pos: PointN, size: PointN, objs: List<GameObject>): this(pos, size) {
        objects.addAll(objs)
    }

    fun init() {
        fun initWithFollowers(obj: GameObject) {
            obj.init()
            obj.followers.forEach { initWithFollowers(it) }
        }

        objects.forEach { initWithFollowers(it) }
    }

    fun next() {
        objects.addAll(new_objects)
        new_objects.clear()

        init()

        objects.forEach { it.stats.roomPOS = pos }
        objects.forEach { it.next0WithFollowers() }
        objects.forEach { it.nextTimeWithFollowers() }

        val all = getAllObjects()
        nextMoveOld(all)
        nextActivate(all)

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

    private fun getAllObjects(): java.util.ArrayList<GameObject> {
        val all = ArrayList<GameObject>()
        fun addInList(obj: GameObject) {
            all.add(obj)
            obj.followers.forEach { addInList(it) }
        }
        objects.forEach { addInList(it) }

        return all
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
        for (o in objects) objs.add(o.toEntry())

        return FGRoomEntry(PointN(pos), PointN(size), objs)
    }
}
