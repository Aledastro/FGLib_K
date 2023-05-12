package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

interface WorldUtils {
    companion object {
        fun drawBounds(pos: PointN = PointN.ZERO) {
            val STEP = PointN(1.0, 1.0)
            Platform.graphics.layer = DrawLayer.CAMERA_FOLLOW

            for(o in World.active_room.objects) {
                val c = if(o.stats.fly) Color.color(1.0, 1.0, 0.2, 0.7) else Color.color(1.0, 0.2, 1.0, 0.7)
                Platform.graphics.fill.oval(pos + o.stats.POS - STEP*2, STEP*4, c)
            }

            val colors = arrayOf(
                Color.RED,
                Color.ORANGERED,
                Color.BLUE,
                Color.GREEN)

            Platform.graphics.setStroke(2.0)
            for(o in World.active_room.objects) {
                for(i in 0 until BoundsBox.SIZE) {
                    val bs = o.bounds[i] ?: continue
                    for(el in bs().elements) {
                        Platform.graphics.fill.draw(pos + o.stats.POS, el.shape, FGUtils.transparent(colors[i], 0.1))
                        Platform.graphics.stroke.draw(pos + o.stats.POS, el.shape, FGUtils.transparent(colors[i], 0.6))
                        Platform.graphics.stroke.line(
                            pos + o.stats.POS + el.shape.L,
                            el.shape.S,
                            FGUtils.transparent(colors[i], 0.5))
                    }
                }
            }
        }

        private var ids_time = 0
        private var fps = 6000.0
        private var maxRam = 0L
        private var freeRam = 0L
        private var ram = 0L
        private var last = System.currentTimeMillis()
        private var time = 0L

        fun drawDebug(draw_pos: PointN, room: Room) {
            Platform.graphics.layer = DrawLayer.CAMERA_FOLLOW
            Platform.graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 12.0)

            val b = (ids_time%20 == 0)
            if(b) maxRam = Runtime.getRuntime().totalMemory()
            if(b) freeRam = Runtime.getRuntime().freeMemory()
            if(b) ram = maxRam - freeRam
            val p = draw_pos + room.size.XP + PointN(10, 0)

            Platform.graphics.fill.text(p + PointN(0, 20), "size: ${room.objects.size}", Color.DARKBLUE)
            Platform.graphics.fill.text(
                p + PointN(0, 40),
                "ram (MB): ${ram/1000_000}/${maxRam/1000_000}",
                Color.DARKBLUE)
            Platform.graphics.fill.text(
                p + PointN(0, 60),
                "ram (KB) per obj: ${if(room.objects.size != 0) (ram/1000/room.objects.size).toInt() else 0}",
                Color.DARKBLUE)
            time = System.currentTimeMillis() - last
            last = System.currentTimeMillis()
            fps += (1000.0/time)
            fps *= 0.99
            Platform.graphics.fill.text(p + PointN(0, 80), "FPS: ${(fps/100).toInt()}", Color.DARKBLUE)

            for(index in 0 until BoundsBox.SIZE) {
                var bs_n = 0
                room.objects.forEach { o ->
                    val bs = o.bounds[index]
                    if(bs != null) bs_n++
                }
                Platform.graphics.fill.text(
                    p + PointN(0, 100 + index*20), "bounds[${BoundsBox.name(index)}]: $bs_n", Color.DARKBLUE)
            }
            ids_time++
        }
    }
}