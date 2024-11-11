package com.uzery.fglib.core.room

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.core.component.bounds.BoundsElement
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.FGLibSettings.ROOM_ACTIVATE_GRID
import com.uzery.fglib.core.program.Platform.render_camera
import com.uzery.fglib.utils.BoundsUtils
import com.uzery.fglib.utils.CollisionUtils.MAX_MOVE_K
import com.uzery.fglib.utils.CollisionUtils.SUPER_K
import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.data.file.ConstL
import com.uzery.fglib.utils.data.getter.value.PosValue
import com.uzery.fglib.utils.data.getter.value.SizeValue
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import com.uzery.fglib.utils.struct.num.IntI
import kotlin.math.sign

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

        val all = getAllObjects()
        nextMoveOld(all)
        nextActivate(all)

        objects.forEach { it.nextTimeWithFollowers() }

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

    companion object {
        fun drawVisuals(
            draw_pos: PointN,
            vis: ArrayList<Visualiser>,
            pos_map: HashMap<Visualiser, PointN>,
            sort_map: HashMap<Visualiser, PointN>
        ) {
            vis.sortWith { v1, v2 ->
                if (v1.drawLayer().sort != v2.drawLayer().sort) {
                    (v1.drawLayer().sort-v2.drawLayer().sort).sign.toInt()
                } else {
                    render_camera.sort(sort_map[v1]!!, sort_map[v2]!!)
                }
            }
            vis.forEach { visual ->
                visual.drawWithDefaults(draw_pos+render_camera[pos_map[visual]!!])
            }
        }

        fun addObjVis(
            vis: ArrayList<Visualiser>,
            pos_map: HashMap<Visualiser, PointN>,
            sort_map: HashMap<Visualiser, PointN>,
            obj: GameObject
        ) {
            vis.addAll(obj.visuals)

            obj.visuals.forEach { v ->
                pos_map[v] = obj.pos_with_owners+obj.main_owner.stats.roomPOS
            }
            obj.visuals.forEach { v ->
                val sort_p = v.sortPOS+obj.stats.sortPOS
                sort_map[v] = pos_map[v]!!+sort_p
            }

            obj.followers.forEach { addObjVis(vis, pos_map, sort_map, it) }
        }
    }

    fun add(vararg objs: GameObject) = new_objects.addAll(objs)
    fun remove(vararg objs: GameObject) = old_objects.addAll(objs)

    fun add(objs: List<GameObject>) = new_objects.addAll(objs)
    fun remove(objs: List<GameObject>) = old_objects.addAll(objs)

    private fun nextMoveOld(objs: ArrayList<GameObject>) {
        val red_bounds = ArrayList<Bounds>()
        val pos = ArrayList<PointN>()

        objs.forEach {
            val bs = it.bounds.red
            if (!bs.empty) {
                red_bounds.add(bs)
                pos.add(it.pos_with_owners)
            }
        }

        for (obj in objs) {
            if (obj.tagged("#immovable")) continue
            obj.stats.lPOS = obj.stats.POS
            val move_bs = obj.bounds.orange

            if (move_bs.empty) {
                obj.stats.POS += obj.stats.nPOS
                continue
            }

            fun maxMove(move_p: PointN): Double {
                if (red_bounds.isEmpty()) return MAX_MOVE_K

                return red_bounds.indices.minOf {
                    BoundsUtils.maxMoveOld(red_bounds[it], move_bs, pos[it], obj.pos_with_owners, move_p)
                }
            }

            fun move(move_p: PointN): Double {
                if (move_p.length() < ConstL.LITTLE) return MAX_MOVE_K

                val mm = maxMove(move_p*SUPER_K)
                obj.stats.POS += move_p*mm
                return mm
            }

            val min_d = move(obj.stats.nPOS)

            val is_move_complete = min_d == MAX_MOVE_K

            obj.stats.fly = is_move_complete

            var np = obj.stats.nPOS*(1-min_d)

            if (obj.stats.sticky) {
                val sticky_by_default = false
                val sticky = PointN(Array(np.dim) { i ->
                    if (obj.stats.sticky_by[i] ?: sticky_by_default) 1.0 else 0.0
                })
                np *= sticky
            }

            obj.stats.fly_by = Array(np.dim) { false }

            for (level in 0..<np.dim) {
                val m = move(np.separate(level))

                obj.stats.fly_by[level] = m == MAX_MOVE_K
            }
        }
        objs.forEach { it.stats.nPOS = PointN.ZERO }
    }

    private fun nextActivate(objs: ArrayList<GameObject>) {
        val cell_map = HashMap<IntI, ArrayList<GameObject>>()

        val our = objs.filter { obj -> !obj.tagged("#inactive") && obj.main != null }

        our.forEach { obj ->
            val main = obj.main ?: return@forEach
            val L = (obj.stats.POS+main.L)/ROOM_ACTIVATE_GRID
            val R = (obj.stats.POS+main.R)/ROOM_ACTIVATE_GRID
            for (i in L.intX..R.intX) {
                for (j in L.intY..R.intY) {
                    val ii = IntI(i, j)
                    if (ii !in cell_map) cell_map[ii] = ArrayList()
                    cell_map[ii]!!.add(obj)
                }
            }
        }

        val checked = HashSet<Pair<GameObject, GameObject>>()
        fun checkFor(obj1: GameObject, obj2: GameObject) {
            if (obj1 == obj2) return

            val pc = Pair(obj1, obj2)
            if (pc in checked) return
            if (!ShapeUtils.into(obj1.main!!, obj2.main!!)) return

            fun setActivate(o1: GameObject, sh1: BoundsElement, o2: GameObject, sh2: BoundsElement, code: String) {
                val shape1 = sh1.shape() ?: return
                val shape2 = sh2.shape() ?: return
                val copied1 = shape1.copy(o1.pos_with_owners)
                val copied2 = shape2.copy(o2.pos_with_owners)

                if (!ShapeUtils.into(copied1, copied2)) return
                if (sh1.group != sh2.group) return

                o1.activate(InputAction(code, o2, sh1.name, sh2.name))
            }

            obj1.bounds.blue.elements.forEach { el1 ->
                obj2.bounds.main.elements.forEach { el2 ->
                    setActivate(obj1, el1, obj2, el2, "#INTERRUPT")
                    setActivate(obj2, el2, obj1, el1, "#INTERRUPT_I")
                }
            }
            obj1.bounds.main.elements.forEach { el1 ->
                if (!obj1.interact()) return@forEach

                obj2.bounds.green.elements.forEach { el2 ->
                    if (obj1.interact()) {
                        setActivate(obj2, el2, obj1, el1, "#INTERRACT")
                        setActivate(obj1, el1, obj2, el2, "#INTERRACT_I")
                    }
                }
            }
            obj1.bounds.blue.elements.forEach { el1 ->
                obj2.bounds.blue.elements.forEach { el2 ->
                    setActivate(obj1, el1, obj2, el2, "#INTERSECT")
                    setActivate(obj2, el2, obj1, el1, "#INTERSECT_I")
                }
            }
            obj1.bounds.orange.elements.forEach { el1 ->
                obj2.bounds.orange.elements.forEach { el2 ->
                    setActivate(obj1, el1, obj2, el2, "#IMPACT")
                    setActivate(obj2, el2, obj1, el1, "#IMPACT_I")
                }
            }

            checked.add(pc)
        }

        cell_map.values.forEach { cell_objs ->
            for (i in cell_objs.indices) {
                for (j in cell_objs.indices) {
                    checkFor(cell_objs[i], cell_objs[j])
                }
            }
        }
    }

    override fun toString(): String {
        return buildString {
            append(ConstL.FILES_COMMENT)

            append("room: ${PosValue(pos)} ${SizeValue(size)}\n\n")

            for (o in objects) {
                if (!o.isTemp()) append("$o\n")
            }
        }
    }
}
