package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.bounds.BoundsElement
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.data.getter.value.PosValue
import com.uzery.fglib.utils.data.getter.value.SizeValue
import com.uzery.fglib.utils.math.BoundsUtils
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.sign

class Room(val pos: PointN, val size: PointN) {
    //todo private
    val objects = LinkedList<GameObject>()
    private val new_objects = ArrayList<GameObject>()
    private val old_objects = HashSet<GameObject>()

    val main = RectN(pos, size)

    constructor(pos: PointN, size: PointN, objs: List<GameObject>): this(pos, size) {
        objects.addAll(objs)
    }

    fun next() {
        objects.addAll(new_objects)
        new_objects.clear()

        objects.forEach { it.next() }
        nextMoveOld()
        nextActivate()

        objects.forEach { new_objects.addAll(it.children) }
        objects.forEach { it.children.clear() }

        objects.removeIf { it.dead || it.owner != null }

        objects.removeAll(old_objects)
        old_objects.clear()
    }

    fun draw(draw_pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val pos_map = HashMap<Visualiser, PointN>()
        objects.forEach { obj ->
            vis.addAll(obj.visuals)
            obj.visuals.forEach { pos_map[it] = obj.stats.POS }
        }
        vis.sortWith { v1, v2 ->
            when {
                v1.drawLayer().sort != v2.drawLayer().sort -> (v1.drawLayer().sort - v2.drawLayer().sort).toInt()
                else -> sign(pos_map[v1]!!.Y - pos_map[v2]!!.Y).toInt()
            }
        }
        vis.forEach { visual ->
            visual.agc.layer = visual.drawLayer()
            visual.draw(draw_pos + pos_map[visual]!!)
        }
    }


    //todo remove from objects not new_objects
    fun add(vararg objs: GameObject) = new_objects.addAll(objs)
    fun remove(vararg objs: GameObject) = old_objects.addAll(objs)

    fun add(objs: List<GameObject>) = new_objects.addAll(objs)
    fun remove(objs: List<GameObject>) = old_objects.addAll(objs)

    private fun nextMoveOld() {
        val red_bounds = LinkedList<Bounds>()
        val pos = LinkedList<PointN>()
        objects.forEach {
            val bs = it.bounds.red
            if(!bs.isEmpty()) {
                red_bounds.add(bs)
                pos.add(it.stats.POS)
            }
        }
        for(obj in objects) {
            obj.stats.lPOS = obj.stats.POS
            if(obj.tagged("#immovable")) continue
            val move_bs = obj.bounds.orange
            if(move_bs.isEmpty()) continue

            fun maxMove(move_p: PointN): Double {
                if(red_bounds.isEmpty()) return 1.0

                return red_bounds.indices.minOf {
                    BoundsUtils.maxMove(red_bounds[it], move_bs, pos[it], obj.stats.POS, move_p)
                }
            }

            fun move(move_p: PointN): Double {
                val mm = maxMove(move_p)
                obj.stats.POS += move_p*mm*0.999
                return mm
            }

            val min_d = move(obj.stats.nPOS)
            obj.stats.fly = min_d == 1.0
            val np = obj.stats.nPOS*(1 - min_d)

            for(i in 0 until np.dimension()) move(np.separate(i))
        }
        objects.forEach { it.stats.nPOS = PointN.ZERO }
    }

    private fun nextMove() {
        for(o in objects) {
            if(o.tagged("#immovable")) continue
            o.stats.lPOS = o.stats.POS
            val orange = o.bounds.orange
            if(orange.isEmpty()) continue

            fun move(move_p: PointN): Double {
                val mm = objects.minOf {
                    val red = it.bounds.red
                    if(red.isEmpty()) return 1.0
                    BoundsUtils.maxMove(red, orange, it.stats.POS, o.stats.POS, move_p)
                }
                o.stats.POS += move_p*mm
                return mm
            }

            val min_d = move(o.stats.nPOS)
            o.stats.fly = min_d == 1.0
            val np = o.stats.nPOS*(1 - min_d)

            for(i in 0 until np.dimension()) move(np.separate(i))
        }
        objects.forEach { it.stats.nPOS = PointN.ZERO }
    }

    private fun nextActivate() {
        //todo less code

        fun setActivate(
            o1: GameObject,
            sh1: BoundsElement,
            o2: GameObject,
            sh2: BoundsElement,
            code: InputAction.CODE,
            message: String,
        ) {
            val shape1 = sh1.shape() ?: return
            val shape2 = sh2.shape() ?: return
            if(ShapeUtils.into(shape1.copy(o1.stats.POS), shape2.copy(o2.stats.POS))) {
                o1.activate(InputAction(code, "$message | ${sh1.name} ${sh2.name}", o2))
            }
        }

        for(blueObj in objects) {
            if(blueObj.tagged("#inactive")) continue

            val blueBounds = blueObj.bounds.blue
            if(blueBounds.isEmpty()) continue
            for(mainObj in objects) {
                if(mainObj.tagged("#inactive")) continue
                val mainBounds = mainObj.bounds.main
                if(mainBounds.isEmpty()) continue
                blueBounds.elements.forEach { blueElement ->
                    mainBounds.elements.forEach { mainElement ->
                        setActivate(blueObj, blueElement, mainObj, mainElement, InputAction.CODE.INTERRUPT, "#interrupt")
                        setActivate(mainObj, mainElement, blueObj, blueElement, InputAction.CODE.INTERRUPT_I, "#interrupt_I")
                    }
                }
            }
        }

        for(mainObj in objects) {
            if(!mainObj.interact() || mainObj.tagged("#inactive")) continue
            val mainBounds = mainObj.bounds.main
            if(mainBounds.isEmpty()) continue
            for(greenObj in objects) {
                if(greenObj.tagged("#inactive")) continue
                val greenBounds = greenObj.bounds.green
                if(greenBounds.isEmpty()) continue
                greenBounds.elements.forEach { greenElement ->
                    mainBounds.elements.forEach { mainElement ->
                        setActivate(greenObj, greenElement, mainObj, mainElement, InputAction.CODE.INTERACT, "#interact")
                        setActivate(mainObj, mainElement, greenObj, greenElement, InputAction.CODE.INTERACT_I, "#interact_I")
                    }
                }
            }
        }
        for(obj1 in objects) {
            if(obj1.tagged("#inactive")) continue
            val bounds1 = obj1.bounds.orange
            if(bounds1.isEmpty()) continue
            for(obj2 in objects) {
                if(obj1 == obj2 || obj2.tagged("#inactive")) continue
                val bounds2 = obj2.bounds.orange
                if(bounds2.isEmpty()) continue
                bounds2.elements.forEach { element2 ->
                    bounds1.elements.forEach { element1 ->
                        setActivate(obj1, element1, obj2, element2, InputAction.CODE.IMPACT, "#impact")
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

        for(o in objects) {
            o.setValues()
            if(o.name != "temp" && o.name != "temporary") wr.append("$o\n")
        }

        return wr.toString()
    }
}
