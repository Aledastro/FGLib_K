package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.listener.InputAction

/**
 * One of basic [ObjectComponent]
 *
 * Updates active [TempAction] and if it ended, replaces with new [TempAction]
 *
 * Useful to create a complex behaviour, such as state machines
 **/
class Controller(private val f: () -> (() -> TempAction)?): ObjectComponent {
    fun get(): (() -> TempAction)? {
        return f()
    }

    private var temp: TempAction? = null
    fun update() {
        if (temp == null || temp!!.finished) temp = get()?.invoke()
        temp?.next()
    }

    fun activate(action: InputAction) {
        temp?.activate(action)
    }
}
