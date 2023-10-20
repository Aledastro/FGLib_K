package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN
import javafx.scene.input.MouseButton

abstract class UIElement {
    abstract val pos: PointN
    abstract val size: PointN
    abstract val window: RectN

    var showing = false
        private set

    fun show() {
        showing = true
    }

    fun hide() {
        showing = false
    }

    fun switch() {
        showing = !showing
    }

    open val priority = 0
    abstract fun draw()
    abstract fun ifActive()

    abstract fun update()

    protected fun pressed(pos: PointN, size: PointN, button: MouseButton = MouseButton.PRIMARY): Boolean {
        return isAt(pos, size) && mouse.keys.pressed(button)
    }

    protected fun inPressed(pos: PointN, size: PointN, button: MouseButton = MouseButton.PRIMARY): Boolean {
        return isAt(pos, size) && mouse.keys.inPressed(button)
    }

    protected fun rePressed(pos: PointN, size: PointN, button: MouseButton = MouseButton.PRIMARY): Boolean {
        return isAt(pos, size) && mouse.keys.rePressed(button)
    }

    protected fun isAt(pos: PointN, size: PointN): Boolean {
        return RectN(pos, size).into(mouse.pos)
    }

    fun isActive() = isAt(pos, size)
}
