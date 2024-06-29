package com.uzery.fglib.core.obj.controller

import com.uzery.fglib.core.obj.ObjectComponent
import com.uzery.fglib.core.obj.ability.InputAction

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
