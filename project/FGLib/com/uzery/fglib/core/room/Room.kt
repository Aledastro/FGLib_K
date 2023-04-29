package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.BoundsUtils
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.getter.value.SizeValue
import javafx.scene.paint.Color
import java.util.*

class Room(val pos: PointN, val size: PointN) {
    private val objects = LinkedList<GameObject>()
    private val new_objects = ArrayList<GameObject>()

    constructor(pos: PointN, size: PointN, objs: List<GameObject>): this(pos, size) {
        objects.addAll(objs)
    }

    fun next() {
        objects.addAll(new_objects)
        new_objects.clear()

        objects.forEach { o -> o.next() }
        nextMoveOld()
        nextActivate()

        objects.forEach { o -> new_objects.addAll(o.children) }
        objects.forEach { o -> o.children.clear() }

        objects.removeIf { o -> o.dead || o.owner != null }

    }


    private var last = System.currentTimeMillis()
    private var time = 0L
    fun draw(draw_pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val map = HashMap<Visualiser, GameObject>()
        objects.forEach { o ->
            run {
                vis.addAll(o.visuals)
                o.visuals.forEach { v -> map[v] = o }
            }
        }
        vis.sortBy { v -> v.drawLayer().sort }
        vis.forEach { v ->
            run {
                val o = map[v]
                if(o != null) {
                    v.agc().layer = v.drawLayer()
                    v.draw(draw_pos + o.stats.POS)
                } else throw IllegalArgumentException()
            }
        }

        drawDebug()
    }

    private var ids_time = 0
    private var fps = 6000.0
    private var maxRam = 0L
    private var freeRam = 0L
    private var ram = 0L
    private fun drawDebug() {
        Platform.graphics.layer = DrawLayer.CAMERA_OFF
        val b = (ids_time%20 == 0)
        if(b) maxRam = Runtime.getRuntime().totalMemory()
        if(b) freeRam = Runtime.getRuntime().freeMemory()
        if(b) ram = maxRam - freeRam
        val p = PointN(970.0, 50.0)

        Platform.graphics.fill.text(p + PointN(0.0, 20.0), "size: ${objects.size}", Color.DARKBLUE)
        Platform.graphics.fill.text(
            p + PointN(0.0, 40.0),
            "ram (MB): ${ram/1000_000}/${maxRam/1000_000}",
            Color.DARKBLUE)
        Platform.graphics.fill.text(
            p + PointN(0.0, 60.0),
            "ram (KB) per obj: ${if(objects.size != 0) (ram/1000/objects.size).toInt() else 0}",
            Color.DARKBLUE)
        time = System.currentTimeMillis() - last
        last = System.currentTimeMillis()
        fps += (1000.0/time)
        fps *= 0.99
        Platform.graphics.fill.text(p + PointN(0.0, 80.0), "fps: ${(fps/100).toInt()}", Color.DARKBLUE)

        for(index in 0..3) {
            var bs_n = 0
            objects.forEach { o ->
                val bs = o.bounds[index]
                if(bs != null) bs_n++
            }
            Platform.graphics.fill.text(
                p + PointN(0.0, 100.0 + index*20), "bounds[${BoundsBox.name(index)}]: $bs_n", Color.DARKBLUE)
        }
        ids_time++
    }

    fun add(obj: GameObject) = objects.add(obj)

    fun objs(): LinkedList<GameObject> = LinkedList(objects)

    private fun nextMoveOld() {
        val all_bounds = LinkedList<Bounds>()
        val pos = LinkedList<PointN>()
        objects.forEach { o ->
            val bs = o.bounds.red
            if(bs != null) {
                all_bounds.add(bs())
                pos.add(o.stats.POS)
            }
        }
        for(obj in objects) {
            obj.stats.lPOS = obj.stats.POS
            val move_bs = obj.bounds.orange ?: continue

            fun maxMove(move_p: PointN): Double {
                if(all_bounds.isEmpty()) return 1.0

                return (0 until all_bounds.size).minOf { i ->
                    BoundsUtils.maxMove(all_bounds[i], move_bs(), pos[i], obj.stats.POS, move_p)
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
        objects.forEach { o -> o.stats.nPOS = PointN.ZERO }
    }

    private fun nextMove() {
        for(o in objects) {
            o.stats.lPOS = o.stats.POS
            val orange = o.bounds.orange ?: continue

            fun move(move_p: PointN): Double {
                val mm = objects.minOf { r ->
                    val red = r.bounds.red ?: return 1.0
                    BoundsUtils.maxMove(red(), orange(), r.stats.POS, o.stats.POS, move_p)
                }
                o.stats.POS += move_p*mm
                return mm
            }

            val min_d = move(o.stats.nPOS)
            o.stats.fly = min_d == 1.0
            val np = o.stats.nPOS*(1 - min_d)

            for(i in 0 until np.dimension()) move(np.separate(i))
        }
        objects.forEach { o -> o.stats.nPOS = PointN.ZERO }
    }

    private fun nextActivate() {
        for(b in objects) {
            val blue = b.bounds.blue ?: continue
            for(m in objects) {
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
            if(!m.interact()) continue
            val main = m.bounds.main ?: continue
            for(g in objects) {
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
            val orange1 = o1.bounds.orange ?: continue
            for(o2 in objects) {
                if(o1 == o2) continue
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
        wr.append("//Uzery game.Game Studio 2019-2023\n")
        wr.append("//last edit: ").append(FGUtils.time_YMD()).append(" ").append(FGUtils.time_HMS()).append("\n")

        wr.append("room_pos: ").append(PosValue(pos)).append("\n")
        wr.append("room_size: ").append(SizeValue(size)).append("\n\n")

        for(o in objects) {
            o.setValues()
            if(o.name != "temp" && o.name != "temporary") wr.append(o)
        }

        return wr.toString()
    }
}
