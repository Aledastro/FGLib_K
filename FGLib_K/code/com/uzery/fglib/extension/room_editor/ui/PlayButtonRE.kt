package com.uzery.fglib.extension.room_editor.ui

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.World
import com.uzery.fglib.extension.ui.Button
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.input.KeyCode

class PlayButtonRE: Button() {
    override val pos: PointN
        get() = Platform.CANVAS - PointN(110, 110)/Platform.scale

    override val size
        get() = PointN(52, 52)/Platform.scale
    override val window: RectN
        get() = Platform.CANVAS_R

    override val pressed: Boolean
        get() = Platform.keyboard.pressed(KeyCode.CONTROL) && Platform.keyboard.inPressed(KeyCode.SPACE)

    override fun whenPressed(): String {
        World.next()
        return "- play -"
    }

    override fun whenNotPressed() = "- stop - "
}