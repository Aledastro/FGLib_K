package com.uzery.fglib.core.program.extension

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

abstract class UIExtension: Extension() {
    fun mousePos() = mousePos(this)
    fun mouseIn() = mouseIn(this)
    fun mouseAt() = mouseAt(this)
    fun mouseIn(pos: PointN, size: PointN) = mouseIn(this, pos, size)
    fun mouseAt(pos: PointN, size: PointN) = mouseAt(this, pos, size)

    open fun focusAction() = mouse.keys.anyInPressed()

    final override fun update() {
        update0()
        if (isFocused) whenFocused()
        else whenUnfocused()

        if (mouseAt() && focusAction()) {
            Platform.focused_extension = this
        }
    }

    val isFocused
        get() = Platform.focused_extension == this

    open fun update0() {}
    open fun whenFocused() {}
    open fun whenUnfocused() {}

    companion object {
        fun mousePos(e: Extension) = mouse.pos-e.stats.real_pos

        fun mouseIn(e: Extension): Boolean {
            e.stats.bounds?.let { return it.into(mousePos(e)) }
            if (e.stats.size == PointN.ZERO) return false
            return RectN(PointN.ZERO, e.stats.size).into(mousePos(e))
        }
        fun mouseAt(e: Extension): Boolean {
            fun notAtChildren(now: Extension): Boolean {
                return now.children.all { !mouseIn(it) && notAtChildren(it) }
            }
            return mouseIn(e) && notAtChildren(e)
        }

        fun mouseIn(e: Extension, pos: PointN, size: PointN): Boolean {
            if (!mouseIn(e)) return false

            return RectN(pos, size).into(mousePos(e))
        }

        fun mouseAt(e: Extension, pos: PointN, size: PointN): Boolean {
            return mouseIn(e, pos, size) && mouseAt(e)
        }
    }
}
