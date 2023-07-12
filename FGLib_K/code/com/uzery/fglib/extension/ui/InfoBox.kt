package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

abstract class InfoBox: UIElement() {
    abstract val text_data_size: Int
    private val d: Double
        get() = text_draw_size/Platform.scale

    protected open val text_draw_size: Double
        get() = 30.0

    protected open val text_draw_offset: Double
        get() = 0.0

    override fun draw() {
        graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, d)
        for(id in 0 until text_data_size)
            graphics.fill.text(pos + PointN(d/2, d*(1 + text_draw_offset)*(id + 1)), text(id), color(id))
    }

    override fun ifActive() {

    }

    override fun update() {

    }

    abstract fun color(id: Int): Color
    abstract fun text(id: Int): String
}
