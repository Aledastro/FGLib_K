package com.uzery.fglib.core.world

import com.uzery.fglib.core.component.bounds.BoundsBox
import com.uzery.fglib.core.component.bounds.BoundsElement
import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.render_camera
import com.uzery.fglib.core.program.PlatformUpdatable
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFont
import com.uzery.fglib.utils.graphics.data.FGFontWeight
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.OvalN
import com.uzery.fglib.utils.math.geom.shape.RectN
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * TODO("doc")
 **/
object WorldDebugUtils: PlatformUpdatable {
    override fun update() {
        nextDebug()
    }

    init {
        Platform.addUpdatable(this)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

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
            return pos+render_camera[o.pos_with_owners]
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

            val c = when {
                o.stats.fly -> FGColor(1.0, 1.0, 0.2, 0.7)
                o.tagged("#immovable") -> FGColor(0.8, 0.8, 0.8, 0.5)
                else -> FGColor(1.0, 0.2, 1.0, 0.7)
            }
            val n = map[draw_pos]!!
            val id = mapID[o]!!

            if (n == 1) {
                graphics.fill.ovalC(draw_pos, STEP*3, c)
            } else {
                val alpha = PI*2/n*id
                val dPOS = PointN(cos(alpha), sin(alpha))*2.5
                graphics.stroke.width = 0.75
                graphics.stroke.line(draw_pos, dPOS, c.transparent(0.5))
                graphics.fill.ovalC(draw_pos+dPOS, STEP*2, c)
            }

            if (o.stats.sortPOS.length() > 1) {
                graphics.fill.ovalC(draw_pos+render_camera[o.stats.sortPOS], STEP*2, c)
            }

            for (v in o.visuals) {
                if (v.sortPOS.length() > 1) {
                    graphics.fill.ovalC(draw_pos+render_camera[o.stats.sortPOS+v.sortPOS], STEP, c)
                }
            }
        }
    }

    fun drawBoundsFor(o: GameObject, pos: PointN) {
        for (i in 0..<BoundsBox.SIZE) {
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

        graphics.stroke.width = 1.0
        for (el in bs.elements) {
            val sh = el.now ?: continue

            fun p2(p: PointN): PointN {
                return PointN(p.X, p.Y)
            }

            //todo
            val shape = when (sh.code) {
                Shape.Code.RECT -> RectN(p2(sh.L), p2(sh.S))
                Shape.Code.OVAL -> OvalN(p2(sh.L), p2(sh.S))
                Shape.Code.FIGURE -> sh
            }
            val z1 = render_camera[sh.L.ZP]
            val z2 = render_camera[sh.R.ZP]

            if (shape.S.more(PointN.ZERO)) {
                graphics.stroke.line(
                    pos+render_camera[o.pos_with_owners+sh.L],
                    render_camera[sh.R]-render_camera[sh.L],
                    colors_h[color_id].transparent(0.8)
                )
            }

            val draw_pos = pos+render_camera[o.pos_with_owners]

            //graphics.fill.draw(draw_pos+z1, shape, colors[color_id].transparent(0.05))
            graphics.stroke.draw(draw_pos+z1, shape, colors[color_id].transparent(0.3))
            //graphics.fill.draw(draw_pos+z2, shape, colors[color_id].transparent(0.1))
            graphics.stroke.draw(draw_pos+z2, shape, colors[color_id].transparent(0.6))

            if (el.name != BoundsElement.DEFAULT_NAME) {
                graphics.fill.font = FGFont("Arial", 3.0)
                graphics.fill.text(
                    draw_pos+shape.L+PointN(0, -2),
                    el.name,
                    colors[color_id].transparent(0.6)
                )
            }
        }
    }

    fun drawBoundsForOld(o: GameObject, pos: PointN, color_id: Int) {
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

        graphics.stroke.width = 1.0
        for (el in bs.elements) {
            val sh = el.now ?: continue

            fun p2(p: PointN): PointN {
                return PointN(p.X, p.Y)
            }

            //todo
            val shape = when (sh.code) {
                Shape.Code.RECT -> RectN(p2(sh.L), p2(sh.S))
                Shape.Code.OVAL -> OvalN(p2(sh.L), p2(sh.S))
                Shape.Code.FIGURE -> sh
            }

            if (shape.S.more(PointN.ZERO)) {
                graphics.stroke.line(
                    pos+render_camera[o.pos_with_owners]+shape.L,
                    shape.S,
                    colors_h[color_id].transparent(0.8)
                )
            }

            val draw_pos = pos+render_camera[o.pos_with_owners]

            graphics.fill.draw(draw_pos, shape, colors[color_id].transparent(0.1))
            graphics.stroke.draw(draw_pos, shape, colors[color_id].transparent(0.6))

            if (el.name != BoundsElement.DEFAULT_NAME) {
                graphics.fill.font = FGFont("Arial", 3.0)
                graphics.fill.text(
                    draw_pos+shape.L+PointN(0, -2),
                    el.name,
                    colors[color_id].transparent(0.6)
                )
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

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
            val runtime = Runtime.getRuntime()
            maxRam = runtime.totalMemory()
            freeRam = runtime.freeMemory()
            ram = maxRam-freeRam
        }

        val now = System.currentTimeMillis()
        time = now-last
        last = now
        fps += (1000.0/time)
        fps *= 0.99

        ids_time++
    }

    fun nextDebugForRoom(room: Room) {
        if (bs_n[room] == null) bs_n[room] = Array(BoundsBox.SIZE) { 0 }
        for (index in 0..<BoundsBox.SIZE) {
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

        for (index in 0..<BoundsBox.SIZE) {
            graphics.fill.text(
                p+PointN(0, 70+index*10),
                "bounds[${BoundsBox.name(index)}]: ${bs_n[room]!![index]}",
                FGColor.DARKBLUE
            )
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
}
