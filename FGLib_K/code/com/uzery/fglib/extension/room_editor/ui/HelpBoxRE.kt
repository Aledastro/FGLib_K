package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.paint.Color
import java.util.*

class HelpBoxRE(private val data: DataRE): InfoBox() {
    override val text_draw_offset: Double
        get() = 0.1

    override fun draw() {
        graphics.fill.rect(pos, size, FGUtils.transparent(Color.BEIGE, 0.7))
        super.draw()
    }

    override fun getL(): ArrayList<String> {
        val res = LinkedList<String>()

        res.add("-----------------------------------------")
        res.add("")

        res.add("pressing [F1] = Help window")
        res.add("")
        res.add("[F3] = show/hide lines")
        res.add("[F4] = show/hide higher layers")
        res.add("[F5] = show/hide world info")
        res.add("[F6] = show/hide room info")
        res.add("")
        res.add("pressing [SHIFT] = show choose object window")
        res.add("[CONTROL] + [SHIFT] + [S] = save rooms")
        res.add("")
        res.add("in CANVAS:")
        res.add("[LMB] = add chosen obj")
        res.add("[RMB] = remove objs in cell")
        res.add("[MOUSE WHEEL] = scale canvas")
        res.add("[ALT] = select objs")
        res.add("[MINUS] = minus add size")
        res.add("[PLUS/EQUALS] = plus add size")
        res.add("[P] = change GRID offset")
        res.add("[CONTROL] + [TAB] = show/hide bounds")
        res.add("[CONTROL] + [M] = change edit mode: FOCUSED, ALL_ROOMS, OVERVIEW")
        res.add("[CONTROL] + [SPACE] = run simulation (bugged now)")
        res.add("")
        res.add("[ARROWS] = move select objs")
        res.add("[CONTROL] + [ARROWS] = move edit room")
        res.add("[CONTROL] + [SHIFT] + [ARROWS] = move all rooms")
        res.add("")
        res.add("[SHIFT] + [ARROWS] = change room pos")
        res.add("[ALT] + [ARROWS] = change room size")
        res.add("")
        res.add("[CONTROL] + [IJKL] = change edit room by neighbours")
        res.add("[ALT] + [IJKL] = change edit room by index")
        res.add("")
        res.add("in REDACT FIELD:")
        res.add("[ARROWS] = change caret pos")
        res.add("[ESCAPE] = reset changes")
        res.add("[ENTER] = save changes")
        res.add("[BACKSPACE] = delete char before caret")

        res.add("")
        res.add("-----------------------------------------")

        for (i in 1..12) res.addFirst("")
        res.replaceAll { " ".repeat(150)+it }

        return ArrayList<String>().also { it.addAll(res) }
    }

    override fun color(id: Int): Color {
        return when (id) {
            0 -> Color.gray(0.2, 0.9)
            else -> Color.gray(0.2, 0.9)
        }
    }

    override val pos
        get() = PointN.ZERO
    override val size: PointN
        get() = CANVAS

    override val priority = 3

    override val window: RectN
        get() = CANVAS_R
}
