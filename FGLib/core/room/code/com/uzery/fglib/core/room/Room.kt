package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.BoundsUtils
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import java.util.*

class Room(private val width: Int, private val height: Int) {
    private val objects = LinkedList<GameObject>()
    private val new_objects = ArrayList<GameObject>()

    constructor(width: Int, height: Int, objs: List<GameObject>): this(width, height) {
        objects.addAll(objs)
    }

    fun next() {
        objects.addAll(new_objects)
        new_objects.clear()

        objects.forEach { o -> o.next() }
        nextMove()
        nextActivate()

        objects.forEach { o -> new_objects.addAll(o.children) }
        objects.forEach { o -> o.children.clear() }

        objects.removeIf { o -> o.stats.dead }
    }


    private var last = System.currentTimeMillis()
    private var time = 0L
    fun draw() {
        val vis = ArrayList<Visualiser>()
        val map = HashMap<Visualiser, GameObject>()
        objects.forEach { o ->
            run {
                vis.addAll(o.visuals)
                o.visuals.forEach { v -> map[v] = o }
            }
        }
        vis.sortBy { v -> v.drawLayer().z }
        vis.forEach { v ->
            run {
                val o = map[v]
                if(o != null) v.draw(o.stats.POS)
                else throw IllegalArgumentException()
            }
        }

        val maxRam = Runtime.getRuntime().totalMemory()
        val freeRam = Runtime.getRuntime().freeMemory()
        val ram = maxRam - freeRam
        Platform.graphics.fill.text(PointN(500.0, 20.0), "size: ${objects.size}", Color.BURLYWOOD)
        Platform.graphics.fill.text(PointN(500.0, 40.0), "ram: ${ram/1000_000}/${maxRam/1000_000}", Color.BURLYWOOD)
        Platform.graphics.fill.text(
            PointN(500.0, 60.0),
            "ram per obj: ${if(objects.size != 0) (ram/1000/objects.size)*0.001 else 0}",
            Color.BURLYWOOD)
        time = System.currentTimeMillis() - last
        last = System.currentTimeMillis()
        val fps = 1000.0/time
        Platform.graphics.fill.text(PointN(500.0, 80.0), "fps: $fps", Color.BURLYWOOD)
    }

    fun add(obj: GameObject) = objects.add(obj)

    fun objs(): LinkedList<GameObject> = LinkedList(objects)

    private fun nextMove() {
        val all_bounds = LinkedList<Bounds>()
        val map = HashMap<Bounds, GameObject>()
        objects.forEach { o ->
            val bs = o.bounds.red
            if(bs != null) {
                all_bounds.add(bs())
                map[bs()] = o
            }
        }
        for(obj in objects) {
            obj.stats.lPOS = obj.stats.POS

            fun move(move_p: PointN): Double {
                val move_bs = obj.bounds.orange

                var min_d = 1.0
                if(move_bs != null) {
                    for(bs in all_bounds) {
                        val stay = map[bs] ?: throw java.lang.IllegalArgumentException()
                        min_d = min_d.coerceAtMost(
                            BoundsUtils.maxMove(
                                bs,
                                move_bs(),
                                stay.stats.POS,
                                obj.stats.POS,
                                move_p))
                    }
                }
                obj.stats.POS += move_p*min_d
                return min_d
            }

            val min_d = move(obj.stats.nPOS)
            obj.stats.fly = min_d == 1.0
            val np = obj.stats.nPOS*(1 - min_d)

            for(i in 0 until np.dimension()) move(np.separate(i))

        }
        objects.forEach { o -> o.stats.nPOS = PointN.ZERO }
    }
    private fun nextActivate() {
        objects.forEach {  }
    }

    override fun toString(): String {
        val wr = StringBuilder()
        wr.append("//Uzery Game Studio 2019-2023\n")
        wr.append("//last edit: ").append(FGUtils.time_YMD()).append(" ").append(FGUtils.time_HMS()).append("\n")

        wr.append("room_width: ").append(width).append("\n")
        wr.append("room_height: ").append(height).append("\n\n")

        for(o in objects) {
            o.setValues()
            if(o.name != "temp" && o.name != "temporary") wr.append(o)
        }

        return wr.toString()
    }
}
