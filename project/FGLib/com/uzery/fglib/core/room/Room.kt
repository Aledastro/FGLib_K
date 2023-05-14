package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.math.BoundsUtils
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.getter.value.SizeValue
import java.util.*

class Room(val pos: PointN, val size: PointN) {
    //todo private
    val objects = LinkedList<GameObject>()
    private val new_objects = ArrayList<GameObject>()

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

    }

    fun draw(draw_pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val map = HashMap<Visualiser, GameObject>()
        objects.forEach { obj ->
            run {
                vis.addAll(obj.visuals)
                obj.visuals.forEach { map[it] = obj }
            }
        }
        vis.sortBy { it.drawLayer().sort }
        vis.forEach { visual ->
            run {
                val o = map[visual]
                if(o != null) {
                    visual.agc().layer = visual.drawLayer()
                    visual.draw(draw_pos + o.stats.POS)
                } else throw IllegalArgumentException()
            }
        }
    }


    //todo remove from objects not new_objects
    fun add(obj: GameObject) = new_objects.add(obj)
    fun remove(obj: GameObject) = objects.remove(obj)

    private fun nextMoveOld() {
        val all_bounds = LinkedList<Bounds>()
        val pos = LinkedList<PointN>()
        objects.forEach {
            val bs = it.bounds.red
            if(bs != null) {
                all_bounds.add(bs())
                pos.add(it.stats.POS)
            }
        }
        for(obj in objects) {
            obj.stats.lPOS = obj.stats.POS
            val move_bs = obj.bounds.orange ?: continue

            fun maxMove(move_p: PointN): Double {
                if(all_bounds.isEmpty()) return 1.0

                return all_bounds.indices.minOf {
                    BoundsUtils.maxMove(all_bounds[it], move_bs(), pos[it], obj.stats.POS, move_p)
                }
            }

            fun move(move_p: PointN): Double {
                val mm = maxMove(move_p)
                obj.stats.POS += move_p*mm
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
            val orange = o.bounds.orange ?: continue

            fun move(move_p: PointN): Double {
                val mm = objects.minOf {
                    val red = it.bounds.red ?: return 1.0
                    BoundsUtils.maxMove(red(), orange(), it.stats.POS, o.stats.POS, move_p)
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
        for(b in objects) {
            if(b.tagged("#inactive")) continue

            val blue = b.bounds.blue ?: continue
            for(m in objects) {
                if(m.tagged("#inactive")) continue
                val main = m.bounds.main ?: continue
                blue().elements.forEach { blueE ->
                    main().elements.forEach { mainE ->
                        if(ShapeUtils.into(mainE.shape.copy(m.stats.POS), blueE.shape.copy(b.stats.POS))) {
                            b.activate(
                                InputAction(
                                    InputAction.CODE.INTERRUPT,
                                    "interrupt | ${blueE.name} ${mainE.name}",
                                    m))
                            m.activate(
                                InputAction(
                                    InputAction.CODE.INTERRUPT_I,
                                    "interrupt_I | ${mainE.name} ${blueE.name}",
                                    b))
                        }
                    }
                }
            }
        }

        for(m in objects) {
            if(!m.interact() || m.tagged("#inactive")) continue
            val main = m.bounds.main ?: continue
            for(g in objects) {
                if(g.tagged("#inactive")) continue
                val green = g.bounds.green ?: continue
                green().elements.forEach { greenE ->
                    main().elements.forEach { mainE ->
                        if(ShapeUtils.into(mainE.shape.copy(m.stats.POS), greenE.shape.copy(g.stats.POS))) {
                            g.activate(
                                InputAction(
                                    InputAction.CODE.INTERACT,
                                    "interact | ${greenE.name} ${mainE.name}",
                                    m))
                            m.activate(
                                InputAction(
                                    InputAction.CODE.INTERACT_I,
                                    "interact_I | ${mainE.name} ${greenE.name}",
                                    g))
                        }
                    }
                }
            }
        }
        for(o1 in objects) {
            if(o1.tagged("#inactive")) continue
            val orange1 = o1.bounds.orange ?: continue
            for(o2 in objects) {
                if(o1 == o2 || o2.tagged("#inactive")) continue
                val orange2 = o2.bounds.orange ?: continue
                orange2().elements.forEach { orange2E ->
                    orange1().elements.forEach { orange1E ->
                        if(ShapeUtils.into(orange1E.shape.copy(o1.stats.POS), orange2E.shape.copy(o2.stats.POS))) {
                            o1.activate(
                                InputAction(
                                    InputAction.CODE.IMPACT,
                                    "impact | ${orange1E.name} ${orange2E.name}",
                                    o2))
                        }
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
