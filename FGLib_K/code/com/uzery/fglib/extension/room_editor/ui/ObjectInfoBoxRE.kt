package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import java.util.*

class ObjectInfoBoxRE(val data: DataRE, private val pair: Pair<GameObject, Room>): InfoBox() {
    override fun getL(): ArrayList<String> {
        val res = ArrayList<String>()
        val s = pair.first.toString()
        if (':' !in s) {
            res.add("object: $s")
            return res
        }
        val name = FGUtils.subBefore(s, ":")
        val args = FGUtils.subAfter(s, ":")
        val t = StringTokenizer(args, "]")

        res.add("object: $name")
        res.add("")

        while (t.hasMoreTokens()) {
            res.add((t.nextToken()+"]\n").substring(1))
        }
        return res
    }

    var dead = false
        private set

    override val text_draw_offset: Double
        get() = 0.1

    override fun color(id: Int): Color {
        return Color.gray(0.2, 0.9)
    }

    override fun ifActive() {
        super.ifActive()
        if (mouse.keys.inPressed(MouseButton.SECONDARY)) {
            dead = true
        }
        if (mouse.keys.inPressed(MouseButton.PRIMARY)) {
            data.redact_pair = pair
        }
    }

    override fun draw() {
        graphics.fill.rect(pos, size, FGUtils.transparent(Color.DARKBLUE, 0.2))
        super.draw()

        graphics.setStroke(2.0)
        val s = 4
        val col = if (isAt()) Color.color(0.6, 0.1, 0.1, 1.0) else Color.color(0.9, 0.9, 0.9, 0.0)
        val posS = pos+size*PointN(1.0, 0.5)-PointN(10+s/2, s/2)
        graphics.stroke.line(posS, PointN(s, s), col)
        graphics.stroke.line(posS+PointN(s, 0), PointN(-s, s), col)
    }

    override val pos = (Platform.CANVAS-size).XP+PointN(-data.OFFSET-5, 70.0)
    override val size
        get() = PointN(186.0-10, y_size*(text_data_size+1.5))
    override val window: RectN
        get() = Platform.CANVAS_R

    override val priority = 1
}
