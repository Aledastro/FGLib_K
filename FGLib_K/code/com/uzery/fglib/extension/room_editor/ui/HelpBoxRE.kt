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
import javafx.scene.text.FontWeight
import java.util.*

class HelpBoxRE(private val data: DataRE): InfoBox() {
    override val text_draw_offset: Double
        get() = 0.1

    override fun draw() {
        graphics.fill.rect(pos, size, FGUtils.transparent(Color.BEIGE, 0.7))
        graphics.fill.font("TimesNewRoman", text_draw_size, FontWeight.BOLD)


        for (id in 0 until text_data_size){
            val offset_x = PointN(320, 60)+PointN(text_draw_size, y_size*(id+1.5))
            if(id <= 1 || id >= text_data_size-2){
                graphics.fill.textC(pos+size.XP/2+offset_x.YP, text(id), color(id))
                continue
            }

            graphics.fill.textL(pos+offset_x, "| "+text(id).substringAfter("="), color(id))
            graphics.fill.textR(pos+size.XP+offset_x*PointN(-1,1), text(id).substringBefore("=")+" |", color(id))
        }
    }

    override fun getL(): ArrayList<String> {
        val res = LinkedList<String>()

        res.add("------------------------------o-o-o------------------------------")
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
        res.add("[PAGE_UP/PAGE_DOWN] = change world")
        res.add("")
        res.add("=in CANVAS:")
        res.add("[LMB] = add chosen obj")
        res.add("[RMB] = remove objs in cell")
        res.add("[MOUSE WHEEL] = scale canvas")
        res.add("[SPACE] + [LMB/RMB] = move canvas")
        res.add("[CONTROL] + [Z] = undo action")
        res.add("")
        res.add("[CONTROL] + [TAB] = show/hide bounds")
        res.add("[CONTROL] + [M] = change redact mode")
        res.add("[CONTROL] + [SPACE] = run simulation (bugged now)")
        res.add("")
        res.add("[ALT] + dragging [LMB] = select objs")
        res.add("[CONTROL] + [ALT] + dragging [LMB] = add to selected objs")
        res.add("[CONTROL] + [ALT] + dragging [RMB] = remove from selected objs")
        res.add("[DELETE] = remove select objs")
        res.add("")
        res.add("[MINUS] = minus add size")
        res.add("[PLUS/EQUALS] = plus add size")
        res.add("[P] = change GRID offset")
        res.add("")
        res.add("[ARROWS] / [CONTROL] + [RMB] = move select objs")
        res.add("[CONTROL] + [LMB] = copy select objs")
        res.add("[CONTROL] + [ARROWS] = move edit room")
        res.add("[CONTROL] + [SHIFT] + [ARROWS] = move all rooms")
        res.add("")
        res.add("[R] + [LMB] on edges = change edit room pos/size")
        res.add("")
        res.add("[WASD] = change edit room by neighbours")
        res.add("[X] + [ARROWS] = change edit room by index")
        res.add("")
        res.add("=in REDACT FIELD:")
        res.add("[ARROWS/MOUSE POS] = change caret pos")
        res.add("[ESCAPE] = reset changes")
        res.add("[ENTER] = save changes")
        res.add("[BACKSPACE] = delete char before caret")

        res.add("")
        res.add("------------------------------o-o-o------------------------------")

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
