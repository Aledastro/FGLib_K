package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.input.MouseButton

abstract class UIElement {
    abstract val pos: PointN
    abstract val size: PointN
    open val priority = 0
    abstract fun draw()
    abstract fun ifActive()

    abstract fun update()

    protected fun pressed(pos: PointN, size: PointN, button: MouseButton = MouseButton.PRIMARY): Boolean {
        return RectN(pos, size).into(mouse.pos()) && mouse_keys.pressed(button)
    }

    protected fun inPressed(pos: PointN, size: PointN, button: MouseButton = MouseButton.PRIMARY): Boolean {
        return RectN(pos, size).into(mouse.pos()) && mouse_keys.inPressed(button)
    }

    protected fun rePressed(pos: PointN, size: PointN, button: MouseButton = MouseButton.PRIMARY): Boolean {
        return RectN(pos, size).into(mouse.pos()) && mouse_keys.rePressed(button)
    }
}