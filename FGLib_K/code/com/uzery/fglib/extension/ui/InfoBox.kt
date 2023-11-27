package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight

abstract class InfoBox: UIElement() {
    var text = ArrayList<String>()

    val y_size: Double
        get() = text_draw_size*(1+text_draw_offset)

    protected open val text_draw_size: Double
        get() = 30.0/4

    protected open val text_draw_offset: Double
        get() = 0.0

    override fun draw() {
        graphics.fill.font("TimesNewRoman", text_draw_size, FontWeight.BOLD)
        for (id in 0 until text_data_size)
            graphics.fill.text(pos+PointN(text_draw_size, y_size*(id+1.5)), text(id), color(id))
    }

    override fun ifActive() {

    }

    override fun update() {
        text = getL()
    }

    abstract fun getL(): ArrayList<String>

    abstract fun color(id: Int): Color
    fun text(id: Int): String {
        return text[id]
    }

    val text_data_size: Int
        get() = text.size
}
