package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

abstract class InfoBox: UIElement() {
    abstract val text_size: Int
    private val d
        get() = 20.0/Platform.scale

    override fun draw() {
        graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, d*3/2)
        for(id in 0 until text_size)
            graphics.fill.text(pos + PointN(d/2, d*3/2*(id + 1)), text(id), color(id))
    }

    override fun ifActive() {

    }

    override fun update() {

    }

    abstract fun color(id: Int): Color
    abstract fun text(id: Int): String
}
