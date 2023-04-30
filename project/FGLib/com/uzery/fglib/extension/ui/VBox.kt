package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.util.*

abstract class VBox(private val full: Int, private val rows: Int): UIElement() {
    private val sizeOne = PointN(50.0, 50.0)
    final override val size: PointN
        get() = PointN(70.0*rows, 80.0*((full - 1)/rows + 1))
    private var name = LinkedList<String>()

    var value = false

    var select = 0

    abstract fun setNames(id: Int): String

    abstract fun draw(pos: PointN, id: Int)

    override fun draw() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.setStroke(2.0)


        for(i in 0 until full) {
            name.add(this.setNames(i))
        }
        for(id in 0 until full) {
            val ps = pos + fromID(id)
            graphics.fill.rect(ps, sizeOne, Color.DARKBLUE.interpolate(Color.TRANSPARENT, 0.9))

            graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 12.0)
            graphics.fill.textC(ps + PointN(sizeOne).apply { X /= 2;Y *= 1.3 }, name[id], Color.DARKBLUE)
            draw(ps + sizeOne/2, id)

            if(id == select) graphics.stroke.rect(ps, sizeOne, Color.DARKBLUE.interpolate(Color.TRANSPARENT, 0.4))
        }
    }

    final override fun update() {
        for(id in 0 until full) {
            if(pressed(pos + fromID(id), sizeOne)) select = id
        }
    }

    fun fromID(id: Int): PointN {
        val line = id/rows
        val row = id%rows
        return PointN(row*70.0, line*80.0)
    }
}