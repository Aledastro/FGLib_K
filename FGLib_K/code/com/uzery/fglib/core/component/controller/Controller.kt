package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.listener.InputAction

/**
 * TODO("doc")
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
