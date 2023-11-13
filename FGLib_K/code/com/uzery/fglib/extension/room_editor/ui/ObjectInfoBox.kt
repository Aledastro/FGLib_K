package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.InfoBox
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import java.util.LinkedList

class ObjectInfoBox(val data: DataRE, private val info: LinkedList<String>): InfoBox() {
    override val text_data_size = info.size

    var dead = false
        private set

    override val text_draw_offset: Double
        get() = 0.1

    override fun color(id: Int): Color {
        return Color.PURPLE
    }

    override fun text(id: Int): String {
        return info[id]
    }

    override fun ifActive() {
        super.ifActive()
        if(mouse.keys.inPressed(MouseButton.SECONDARY)){
            dead = true
        }
    }

    override fun draw() {
        super.draw()
        graphics.setStroke(2.0)
        val s = 4
        val col = Color.color(0.4,0.0,0.0,0.8)
        val posS = pos + size*PointN(1.0, 0.5) - PointN(10+s/2,s/2)
        graphics.stroke.line(posS, PointN(s,s), col)
        graphics.stroke.line(posS + PointN(s,0), PointN(-s,s), col)
    }

    override val pos = (Platform.CANVAS-size).XP+PointN(-data.OFFSET, 70.0)
    override val size
        get() = PointN(350.0/Platform.scale, y_size*(text_data_size+1.5))
    override val window: RectN
        get() = Platform.CANVAS_R
}
