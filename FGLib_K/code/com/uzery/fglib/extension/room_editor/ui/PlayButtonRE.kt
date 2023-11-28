package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.extension.room_editor.DataRE
import com.uzery.fglib.extension.ui.Button
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.KeyCode

class PlayButtonRE(val data: DataRE): Button() {
    override val pos: PointN
        get() = CANVAS-PointN(110, 110)/Platform.scale

    override val size
        get() = PointN(52, 52)/Platform.scale
    override val window: RectN
        get() = CANVAS_R

    override val pressed: Boolean
        get() = keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.SPACE)

    override fun whenPressed(): String {
        data.world_play = true
        return "- play -"
    }

    override fun whenNotPressed(): String {
        data.world_play = false
        return "- stop - "
    }
}
