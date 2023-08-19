package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight

abstract class InfoBox: UIElement() {
    abstract val text_data_size: Int
    private val d: Double
        get() = text_draw_size

    protected open val text_draw_size: Double
        get() = 30.0/4

    protected open val text_draw_offset: Double
        get() = 0.0

    override fun draw() {
        graphics.fill.font("TimesNewRoman", d, FontWeight.BOLD)
        for (id in 0 until text_data_size)
            graphics.fill.text(pos+PointN(d, d*(1+text_draw_offset)*(id+1.5)), text(id), color(id))
    }

    override fun ifActive() {

    }

    override fun update() {

    }

    abstract fun color(id: Int): Color
    abstract fun text(id: Int): String
}
