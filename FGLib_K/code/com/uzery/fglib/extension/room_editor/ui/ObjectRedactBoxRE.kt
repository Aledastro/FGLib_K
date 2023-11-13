package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.UIElement
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.FontWeight

class ObjectRedactBoxRE(val data: DataRE): UIElement() {
    var new_obj: String = ""

    override val pos = (Platform.CANVAS-size)/2
    override val size
        get() = PointN(500, 42)
    override val window: RectN
        get() = Platform.CANVAS_R

    override val priority = 2

    override fun draw() {
        graphics.alpha = 0.5
        graphics.fill.rect(pos, size, Color.BEIGE)
        graphics.alpha = 1.0

        graphics.fill.font("TimesNewRoman", 10.0, FontWeight.BOLD)

        graphics.fill.textR(pos+PointN(35,14),"old:", Color.gray(0.2, 0.9))
        graphics.fill.text(pos+PointN(40,14),"${data.redact_obj}", Color.gray(0.2, 0.9))

        val sp = PointN(2, 2)
        graphics.fill.rect(pos+PointN(40,23)-sp, PointN(416, 12)+sp*2, Color.gray(0.2, 0.9))

        graphics.fill.textR(pos+PointN(35, 33), "new:", Color.gray(0.2, 0.9))
        graphics.fill.text(pos+PointN(40, 33), new_obj, Color.gray(0.9, 0.8))


        val sp2 = PointN(5, 5)
        val size1 = PointN(1, 1)*size.Y - sp2*2
        graphics.fill.rect(pos+size.XP+PointN(-sp2.X-size1.X,sp2.Y), size1, Color.gray(0.2, 0.9))

        graphics.fill.font("TimesNewRoman", 14.0, FontWeight.BOLD)
        graphics.alpha = 0.5
        graphics.fill.textR(pos+size-PointN(10.5, 13.5)-sp, "OK", Color.WHITE)
        graphics.alpha = 1.0
    }

    override fun ifActive() {

    }

    var old_redact_obj: GameObject? = null
    override fun update() {
        if(data.redact_obj != old_redact_obj){
            new_obj = data.redact_obj.toString()
        }
        old_redact_obj = data.redact_obj

        if(keyboard.inPressed(KeyCode.ESCAPE)){
            data.redact_obj = null
        }
    }
}
