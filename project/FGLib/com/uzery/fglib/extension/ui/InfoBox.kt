package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

abstract class InfoBox: UIElement() {
    abstract val text_size: Int
    override fun draw() {
        graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 20.0)
        for(id in 0 until text_size) graphics.fill.text(pos + PointN(10.0, 30.0 + 30*id), text(id), color(id))
    }

    override fun ifActive() {

    }

    override fun update() {

    }

    abstract fun color(id: Int): Color
    abstract fun text(id: Int): String
}
