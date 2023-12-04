package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFontWeight
import com.uzery.fglib.utils.math.FGUtils.getPosFrom
import com.uzery.fglib.utils.math.geom.PointN
import java.util.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object WorldUtils {
    fun readInfo(filename: String): Room {
        return readInfo(TextData[filename])
    }

    fun readInfo(input: List<String>): Room {
        if (World.getter == null) throw DebugData.error("getter not loaded")

        val list = LinkedList<String>()
        list.addAll(input)

        val objects = ArrayList<GameObject>()
        var next = ""

        while (next.startsWith("//") || next.isEmpty()) {
            next = list.removeFirst()
        }
        val t = StringTokenizer(next)
        t.nextToken()

        val pos = getPosFrom(t.nextToken()+t.nextToken())
        val size = getPosFrom(t.nextToken()+t.nextToken())
        while (list.isNotEmpty()) {
            next = list.removeFirst()
            if (next.startsWith("//")) continue
            if (next.isNotEmpty()) objects.add(World.getter!![next])
        }

        return Room(pos, size, objects)
    }

    fun drawBounds(objects: List<GameObject>, pos: PointN = PointN.ZERO) {
        val STEP = PointN(1.0, 1.0)
        graphics.layer = DrawLayer.CAMERA_FOLLOW

        val list = ArrayList<GameObject>()
        fun addInList(obj: GameObject) {
            list.add(obj)
            obj.followers.forEach { addInList(it) }
        }
        objects.forEach { addInList(it) }

        val map = HashMap<PointN, Int>()
        val mapID = HashMap<GameObject, Int>()

        fun posFrom(o: GameObject): PointN {
            return pos+o.pos_with_owners
        }

        for (o in list) {
            val draw_pos = posFrom(o)
            if (map[draw_pos] == null) map[draw_pos] = 0
            map[draw_pos] = map[draw_pos]!!+1

            if (mapID[o] == null) mapID[o] = map[draw_pos]!!
        }

        for (o in list) drawBoundsFor(o, pos)

        for (o in list) {
            val draw_pos = posFrom(o)

            val c = if (o.stats.fly) FGColor(1.0, 1.0, 0.2, 0.7) else FGColor(1.0, 0.2, 1.0, 0.7)
            val n = map[draw_pos]!!
            val id = mapID[o]!!

            if (n == 1) {
                graphics.fill.ovalC(draw_pos, STEP*3, c)
            } else {
                val alpha = PI*2/n*id
                val dPOS = PointN(cos(alpha), sin(alpha))*2.5
                graphics.setStroke(0.75)
                graphics.stroke.line(draw_pos, dPOS, c.transparent(0.5))
                graphics.fill.ovalC(draw_pos+dPOS, STEP*2, c)
            }

            if (o.stats.sortPOS.length() > 1) graphics.fill.ovalC(draw_pos+o.stats.sortPOS, STEP, c)
        }
    }

    fun drawBoundsFor(o: GameObject, pos: PointN) {
        for (i in 0 until BoundsBox.SIZE) {
            drawBoundsFor(o, pos, i)
        }
    }

    fun drawBoundsFor(o: GameObject, pos: PointN, color_id: Int) {
        val colors = arrayOf(
            FGColor.RED,
            FGColor.ORANGERED,
            FGColor.BLUE,
            FGColor.GREEN
        )
        val colors_h = arrayOf(
            FGColor.RED.interpolate(FGColor.WHITE, 0.5),
            FGColor.ORANGERED.interpolate(FGColor.WHITE, 0.5),
            FGColor.BLUE.interpolate(FGColor.WHITE, 0.5),
            FGColor.GREEN.interpolate(FGColor.WHITE, 0.5)
        )
        val bs = o.bounds[color_id]
        if (bs.empty) return
        graphics.setStroke(1.0)
        for (el in bs.elements) {
            val shape = el.shape() ?: continue

            if (shape.S.more(PointN.ZERO))
                graphics.stroke.line(
                    pos+o.pos_with_owners+shape.L,
                    shape.S,
                    colors_h[color_id].transparent(0.8)
                )

            graphics.fill.draw(pos+o.pos_with_owners, shape, colors[color_id].transparent(0.1))
            graphics.stroke.draw(pos+o.pos_with_owners, shape, colors[color_id].transparent(0.6))
        }
    }

    private var ids_time = 0
    var fps = 6000.0
        private set
    var maxRam = 0L
        private set
    var freeRam = 0L
        private set
    var ram = 0L
        private set
    var last = System.currentTimeMillis()
        private set
    var time = 0L
        private set

    val bs_n = HashMap<Room, Array<Int>>()

    fun nextDebug() {
        if (ids_time%20 == 0) {
            maxRam = Runtime.getRuntime().totalMemory()
            freeRam = Runtime.getRuntime().freeMemory()
            ram = maxRam-freeRam
        }

        time = System.currentTimeMillis()-last
        last = System.currentTimeMillis()
        fps += (1000.0/time)
        fps *= 0.99

        ids_time++
    }

    fun nextDebugForRoom(room: Room) {
        if (bs_n[room] == null) bs_n[room] = Array(BoundsBox.SIZE) { 0 }
        for (index in 0 until BoundsBox.SIZE) {
            bs_n[room]!![index] = room.objects.count { !it.bounds[index].empty }
        }
    }

    fun drawDebug(draw_pos: PointN, room: Room) {
        graphics.layer = DrawLayer.CAMERA_FOLLOW
        graphics.fill.font("TimesNewRoman", 12.0/2, FGFontWeight.BOLD)

        val p = draw_pos+room.size.XP+PointN(10, 0)

        graphics.fill.text(p+PointN(0, 10), "pos: ${room.pos}", FGColor.DARKBLUE)
        graphics.fill.text(p+PointN(0, 20), "size: ${room.size}", FGColor.DARKBLUE)
        graphics.fill.text(p+PointN(0, 30), "objects: ${room.objects.size}", FGColor.DARKBLUE)
        graphics.fill.text(
            p+PointN(0, 40),
            "ram (MB): ${ram/1000_000}/${maxRam/1000_000}",
            FGColor.DARKBLUE
        )
        graphics.fill.text(
            p+PointN(0, 50),
            "ram (KB) per obj: ${if (room.objects.size != 0) (ram/1000/room.objects.size).toInt() else 0}",
            FGColor.DARKBLUE
        )

        graphics.fill.text(p+PointN(0, 60), "FPS: ${(fps/100).toInt()}", FGColor.DARKBLUE)

        for (index in 0 until BoundsBox.SIZE) {
            graphics.fill.text(
                p+PointN(0, 70+index*10),
                "bounds[${BoundsBox.name(index)}]: ${bs_n[room]!![index]}",
                FGColor.DARKBLUE
            )
        }
    }
}
