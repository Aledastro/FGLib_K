package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

interface WorldUtils {
    companion object {
        fun drawBounds(room: Room, pos: PointN = PointN.ZERO) {
            val STEP = PointN(1.0, 1.0)
            graphics.layer = DrawLayer.CAMERA_FOLLOW

            for(o in room.objects) {
                val c = if(o.stats.fly) Color.color(1.0, 1.0, 0.2, 0.7) else Color.color(1.0, 0.2, 1.0, 0.7)
                graphics.fill.ovalC(pos + o.stats.POS, STEP*3, c)
            }

            for(o in room.objects) drawBoundsFor(o, pos)
        }

        fun drawBoundsFor(o: GameObject, pos: PointN) {
            for(i in 0 until BoundsBox.SIZE) {
                drawBoundsFor(o, pos, i)
            }
        }

        fun drawBoundsFor(o: GameObject, pos: PointN, color_id: Int) {
            val colors = arrayOf(
                Color.RED,
                Color.ORANGERED,
                Color.BLUE,
                Color.GREEN)

            graphics.setStroke(1.0)
            val bs = o.bounds[color_id]
            if(bs.isEmpty()) return
            for(shade in bs.shades) {
                val shape = shade.shape ?: continue
                graphics.fill.draw(pos, shape, FGUtils.transparent(colors[color_id], 0.1))
                graphics.stroke.draw(pos, shape, FGUtils.transparent(colors[color_id], 0.6))
                graphics.stroke.line(pos + shape.L, shape.S, FGUtils.transparent(colors[color_id], 0.5))
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
            val b = (ids_time%20 == 0)
            if(b) maxRam = Runtime.getRuntime().totalMemory()
            if(b) freeRam = Runtime.getRuntime().freeMemory()
            if(b) ram = maxRam - freeRam

            time = System.currentTimeMillis() - last
            last = System.currentTimeMillis()
            fps += (1000.0/time)
            fps *= 0.99

            ids_time++
        }

        fun nextDebugForRoom(room: Room) {
            if(bs_n[room] == null) bs_n[room] = Array(BoundsBox.SIZE) { 0 }
            for(index in 0 until BoundsBox.SIZE) {
                bs_n[room]!![index] = room.objects.count { !it.bounds[index].isEmpty() }
            }
        }

        fun drawDebug(draw_pos: PointN, room: Room) {
            graphics.layer = DrawLayer.CAMERA_FOLLOW
            graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 12.0)

            val p = draw_pos + room.size.XP + PointN(10, 0)

            graphics.fill.text(p + PointN(0, 10), "pos: ${room.pos}", Color.DARKBLUE)
            graphics.fill.text(p + PointN(0, 20), "size: ${room.size}", Color.DARKBLUE)
            graphics.fill.text(p + PointN(0, 30), "objects: ${room.objects.size}", Color.DARKBLUE)
            graphics.fill.text(
                p + PointN(0, 40),
                "ram (MB): ${ram/1000_000}/${maxRam/1000_000}",
                Color.DARKBLUE)
            graphics.fill.text(
                p + PointN(0, 50),
                "ram (KB) per obj: ${if(room.objects.size != 0) (ram/1000/room.objects.size).toInt() else 0}",
                Color.DARKBLUE)

            graphics.fill.text(p + PointN(0, 60), "FPS: ${(fps/100).toInt()}", Color.DARKBLUE)

            for(index in 0 until BoundsBox.SIZE) {
                graphics.fill.text(
                    p + PointN(0, 70 + index*10),
                    "bounds[${BoundsBox.name(index)}]: ${bs_n[room]!![index]}",
                    Color.DARKBLUE)
            }
        }
    }
}