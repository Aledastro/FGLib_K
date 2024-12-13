package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.listener.InputAction

/**
 * [Controller] is one of basic [ObjectComponent]
 *
 * Updates active [TempAction] and if it ended, replaces with new [TempAction]
 **/
class Controller(private val f: () -> (() -> TempAction)?): ObjectComponent {
    fun get(): (() -> TempAction)? {
        return f()
    }

    private var temp: TempAction? = null
    fun update() {
        if (temp == null || temp!!.ends) temp = get()?.invoke()
        temp?.next()
    }

    fun activate(action: InputAction) {
        temp?.activate(action)
    }
}
