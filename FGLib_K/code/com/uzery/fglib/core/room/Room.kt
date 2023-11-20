package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.bounds.BoundsElement
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.data.file.ConstL
import com.uzery.fglib.utils.data.getter.value.PosValue
import com.uzery.fglib.utils.data.getter.value.SizeValue
import com.uzery.fglib.utils.math.BoundsUtils
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import java.util.*
import kotlin.math.sign

class Room(val pos: PointN, val size: PointN) {
    //todo private
    val objects = ArrayList<GameObject>()
    private val new_objects = ArrayList<GameObject>()
    private val old_objects = HashSet<GameObject>()

    val main = RectN(pos, size)

    constructor(pos: PointN, size: PointN, objs: List<GameObject>): this(pos, size) {
        objects.addAll(objs)
    }

    fun init(){
        objects.forEach { it.init() }
    }

    fun next() {
        objects.addAll(new_objects)
        new_objects.clear()

        objects.forEach { it.stats.roomPOS = pos }
        objects.forEach { it.nextWithFollowers() }

        nextMoveOld()
        nextActivate()

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
                when {
                    v1.drawLayer().sort != v2.drawLayer().sort -> (v1.drawLayer().sort-v2.drawLayer().sort).toInt()
                    else -> sign(sort_map[v1]!!.Y-sort_map[v2]!!.Y).toInt()
                }
            }
            vis.forEach { visual ->
                visual.drawWithDefaults(draw_pos+pos_map[visual]!!)
            }
        }

        fun addObjVis(
            vis: ArrayList<Visualiser>,
            pos_map: HashMap<Visualiser, PointN>,
            sort_map: HashMap<Visualiser, PointN>,
            obj: GameObject
        ) {
            vis.addAll(obj.visuals)

            obj.visuals.forEach { pos_map[it] = obj.stats.POS+obj.main_owner.stats.roomPOS }
            obj.visuals.forEach { sort_map[it] = pos_map[it]!!+obj.stats.sortPOS }

            obj.followers.forEach { addObjVis(vis, pos_map, sort_map, it) }
        }
    }

    fun add(vararg objs: GameObject) = new_objects.addAll(objs)
    fun remove(vararg objs: GameObject) = old_objects.addAll(objs)

    fun add(objs: List<GameObject>) = new_objects.addAll(objs)
    fun remove(objs: List<GameObject>) = old_objects.addAll(objs)

    private fun nextMoveOld() {
        val red_bounds = ArrayList<Bounds>()
        val pos = ArrayList<PointN>()

        val list = ArrayList<GameObject>()
        fun addInList(obj: GameObject) {
            list.add(obj)
            obj.followers.forEach { addInList(it) }
        }
        objects.forEach { if(!it.tagged("#immovable")) addInList(it) }

        objects.forEach {
            val bs = it.bounds.red
            if (!bs.empty) {
                red_bounds.add(bs)
                pos.add(it.stats.POS)
            }
        }
        for (obj in list) {
            obj.stats.lPOS = obj.stats.POS
            val move_bs = obj.bounds.orange
            if (move_bs.empty) continue

            fun maxMove(move_p: PointN): Double {
                if (red_bounds.isEmpty()) return 1.0

                return red_bounds.indices.minOf {
                    BoundsUtils.maxMoveOld(red_bounds[it], move_bs, pos[it], obj.stats.POS, move_p)
                }
            }

            fun move(move_p: PointN): Double {
                val mm = maxMove(move_p)
                obj.stats.POS += move_p*mm*(1-ConstL.LITTLE)
                return mm
            }

            val min_d = move(obj.stats.nPOS)
            obj.stats.fly = min_d == 1.0
            val np = obj.stats.nPOS*(1-min_d)

            for (i in 0 until np.dim) move(np.separate(i))
        }
        list.forEach { it.stats.nPOS = PointN.ZERO }
    }

    private fun nextActivate() {
        //todo less code

        val list = ArrayList<GameObject>()
        fun addInList(obj: GameObject) {
            list.add(obj)
            obj.followers.forEach { addInList(it) }
        }
        objects.forEach { if(!it.tagged("#inactive")) addInList(it) }

        fun setActivate(
            o1: GameObject,
            sh1: BoundsElement,
            o2: GameObject,
            sh2: BoundsElement,
            code: String,
        ) {
            val shape1 = sh1.shape() ?: return
            val shape2 = sh2.shape() ?: return
            if (ShapeUtils.into(shape1.copy(o1.stats.POS), shape2.copy(o2.stats.POS))) {
                o1.activate(InputAction(code, o2, "elements | ${sh1.name} ${sh2.name}"))
            }
        }

        for (blueObjID in list.indices) {
            val blueObj = list[blueObjID]

            val blueBounds = blueObj.bounds.blue
            if (blueBounds.empty) continue
            for (mainObj in list) {
                val mainBounds = mainObj.bounds.main
                if (mainBounds.empty) continue
                blueBounds.elements.forEach { blueElement ->
                    mainBounds.elements.forEach { mainElement ->
                        setActivate(
                            blueObj,
                            blueElement,
                            mainObj,
                            mainElement,
                            "#INTERRUPT"
                        )
                        setActivate(
                            mainObj,
                            mainElement,
                            blueObj,
                            blueElement,
                            "#INTERRUPT_I"
                        )
                    }
                }
            }
        }

        for (mainObj in list) {
            if (!mainObj.interact()) continue
            val mainBounds = mainObj.bounds.main
            if (mainBounds.empty) continue
            for (greenObj in list) {
                val greenBounds = greenObj.bounds.green
                if (greenBounds.empty) continue
                greenBounds.elements.forEach { greenElement ->
                    mainBounds.elements.forEach { mainElement ->
                        setActivate(
                            greenObj,
                            greenElement,
                            mainObj,
                            mainElement,
                            "#INTERACT"
                        )
                        setActivate(
                            mainObj,
                            mainElement,
                            greenObj,
                            greenElement,
                            "#INTERACT_I"
                        )
                    }
                }
            }
        }
        for (obj1 in list) {
            val bounds1 = obj1.bounds.orange
            if (bounds1.empty) continue
            for (obj2 in list) {
                if (obj1 == obj2) continue
                val bounds2 = obj2.bounds.orange
                if (bounds2.empty) continue
                bounds2.elements.forEach { element2 ->
                    bounds1.elements.forEach { element1 ->
                        setActivate(obj1, element1, obj2, element2, "#IMPACT")
                    }
                }
            }
        }
    }

    override fun toString(): String {
        val wr = StringBuilder()
        wr.append("//Uzery Game Studio 2019-2023\n")
        wr.append("//last edit: ${FGUtils.time_YMD()} ${FGUtils.time_HMS()}\n\n")

        wr.append("room: ${PosValue(pos)} ${SizeValue(size)}\n\n")

        for (o in objects) {
            if (o.name != "temp") wr.append("$o\n")
        }

        return wr.toString()
    }
}
