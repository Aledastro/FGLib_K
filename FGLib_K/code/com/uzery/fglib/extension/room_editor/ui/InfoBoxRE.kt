package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.paint.Color
import java.util.*

class InfoBoxRE(private val data: DataRE): InfoBox() {
    override val text_draw_offset: Double
        get() = 0.1

    override fun draw() {
        Platform.graphics.alpha = 0.3
        Platform.graphics.fill.rect(pos, size, Color.BEIGE)
        Platform.graphics.alpha = 1.0
        super.draw()
    }

    private fun getL(): List<String> {
        val res = LinkedList<String>()

        res.add("room: ${data.filenames[data.last_edit_n]}")
        res.add("")
        res.add("pos: ${data.last_edit_room.pos}")
        res.add("size: ${data.last_edit_room.size}")
        res.add("objects size: ${data.last_edit_room.objects.size}")
        for (index in 0 until BoundsBox.SIZE) {
            res.add("bounds[${BoundsBox.name(index)}]: ${WorldUtils.bs_n[data.last_edit_room]!![index]}")
        }

        res.add("")
        res.add("")


        val s = data.select_obj.toString()
        if (s.indexOf(':') == -1) {
            res.add("object: $s")
            return res
        }

        val name = FGUtils.subBefore(s, ":")
        val args = FGUtils.subAfter(s, ":")
        val t = StringTokenizer(args, "]")

        res.add("object: $name")
        while (t.hasMoreTokens()) {
            res.add((t.nextToken()+"]\n").substring(1))
        }
        return res
    }

    override fun update() {
        WorldUtils.nextDebugForRoom(data.edit)
    }

    override fun text(id: Int) = getL()[id]
    override val text_data_size: Int
        get() = getL().size

    override fun color(id: Int): Color {
        return when (id) {
            0 -> Color.PURPLE
            else -> Color.PURPLE
        }
    }

    override val pos
        get() = (CANVAS-size).XP+PointN(-data.OFFSET, 70.0)
    override val size
        get() = PointN(350, 450)/Platform.scale
    override val window: RectN
        get() = CANVAS_R
}