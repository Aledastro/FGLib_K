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

class StatusBoxRE(private val data: DataRE): InfoBox() {
    override val text_draw_offset: Double
        get() = 0.1

    val DELAY = 100.0
    val FADE_DELAY = 40.0

    override fun draw() {
        graphics.alpha = 1.0-(data.time-data.save_time-DELAY).coerceIn(0.0, FADE_DELAY)/FADE_DELAY
        graphics.fill.rect(pos, size, FGUtils.transparent(Color.BEIGE, 0.7))
        graphics.fill.font("TimesNewRoman", text_draw_size, FontWeight.BOLD)
        val pos2 = pos+PointN(-8, 5)
        for (id in 0 until text_data_size)
            graphics.fill.text(pos2+PointN(text_draw_size, y_size*(id+0.5)), text(id), color(id))
        graphics.alpha = 1.0
    }

    override val text_draw_size: Double
        get() = 12.5

    override fun getL(): ArrayList<String> {
        val res = ArrayList<String>()
        res.add("saved")
        return res
    }

    override fun color(id: Int): Color {
        return when (id) {
            0 -> Color.gray(0.2, 0.9)
            else -> Color.gray(0.2, 0.9)
        }
    }

    override val pos
        get() = CANVAS.YP-size.YP+PointN(5, -5)
    override val size: PointN
        get() = PointN(42, 16)

    override val priority = 2

    override val window: RectN
        get() = CANVAS_R
}
