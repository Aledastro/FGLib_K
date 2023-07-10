package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import java.util.*

abstract class VBox(protected open val full: Int, protected open val rows: Int): UIElement() {
    abstract val sizeOne: PointN
    private val offset
        get() = sizeOne/5
    final override val size: PointN
        get() = PointN(sizeOne.X*7/5*rows, sizeOne.Y*8/5*((full - 1)/rows + 1))
    private var name = LinkedList<String>()

    override fun update() {
        /*ignore*/
    }

    open fun ifActiveUpdate() {
        /*ignore*/
    }

    var value = false

    var select = 0
        get()=field.coerceIn(0 until full)

    abstract fun setNames(id: Int): String

    abstract fun draw(pos: PointN, id: Int)

    override fun draw() {
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.setStroke(4.0)


        for(i in 0 until full) name.add(this.setNames(i))
        for(id in 0 until full) {
            val ps = pos + fromID(id)
            graphics.fill.rect(ps, sizeOne, FGUtils.transparent(Color.DARKBLUE, 0.1))

            graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 12.0)
            graphics.fill.textC(ps + sizeOne*PointN(0.5, 1.3), name[id], Color.DARKBLUE)
            draw(ps + sizeOne/2, id)

            if(id == select) graphics.stroke.rect(ps, sizeOne, FGUtils.transparent(Color.DARKBLUE, 0.6))
        }
    }

    final override fun ifActive() {
        for(id in 0 until full) {
            if(pressed(pos + fromID(id), sizeOne)) select = id
        }
        ifActiveUpdate()
    }

    private fun fromID(id: Int): PointN {
        val line = id/rows
        val row = id%rows
        return offset + sizeOne*PointN(row*1.4, line*1.6)
    }
}