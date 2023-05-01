package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

abstract class Button: UIElement() {
    private lateinit var name: String
    abstract val pressed: Boolean

    var value = false

    abstract fun whenPressed(): String
    open fun whenNotPressed() = "-"

    override fun draw() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.setStroke(10.0)

        if(value) graphics.stroke.oval(pos, size, Color.DARKBLUE)
        else graphics.stroke.rect(pos, size, Color.DARKBLUE)

        graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 20.0)
        graphics.fill.textC(pos + PointN(size.X*0.5, size.Y*1.6), name, Color.DARKBLUE)
    }

    final override fun ifActive() {
        if(rePressed(pos, size)) value = !value
    }

    final override fun update() {
        if(pressed) value = !value
    }

    fun action() {
        name = if(value) whenPressed()
        else whenNotPressed()
    }
}