package com.uzery.fglib.core.program.extension

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.mouse

abstract class UIExtension: Extension() {
    open fun focusAction() = mouse.keys.anyInPressed()

    final override fun update() {
        update0()
        if (isFocused) whenFocused()
        else whenUnfocused()

        if (mouseAt() && focusAction()) {
            Platform.extension_focused = this
        }
    }

    val isFocused
        get() = Platform.extension_focused == this

    open fun update0() {}
    open fun whenFocused() {}
    open fun whenUnfocused() {}
}
