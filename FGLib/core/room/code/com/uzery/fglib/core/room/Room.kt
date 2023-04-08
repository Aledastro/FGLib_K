package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import java.util.*

class Room {
    private val objects = LinkedList<GameObject>()
    private val new_objects = ArrayList<GameObject>()

    private var width: Int = -1
    private var height: Int = -1

    fun next() {
        objects.addAll(new_objects)
        new_objects.clear()

        objects.forEach { o -> o.next() }
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

    fun add(obj: GameObject) {
        objects.add(obj)
    }

    override fun toString(): String {
        val wr = StringBuilder()
        wr.append("//Uzery Game Studio 2019-2023\n")
        wr.append("//last edit: ").append(FGUtils.time_YMD()).append(" ").append(FGUtils.time_HMS()).append("\n")

        wr.append("room_width: ").append(width).append("\n")
        wr.append("room_height: ").append(height).append("\n\n")

        for(o in objects)
            if(o.name != "temp" && o.name != "temporary") wr.append(o)

        return wr.toString()
    }
}
