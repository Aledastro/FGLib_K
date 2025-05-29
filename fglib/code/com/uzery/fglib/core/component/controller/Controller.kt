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
open class Controller(private val f: () -> (() -> TempAction)?): ObjectComponent {
    fun get(): (() -> TempAction)? {
        return f()
    }

    private var temp: TempAction? = null
    private fun newAction() {
        temp = get()?.invoke()
    }

    fun update() {
        if (temp == null || temp!!.finished) newAction()
        else if (isBroken()) {
            temp?.breakFinish()
            newAction()
        }
        temp?.next()
    }

    fun activate(action: InputAction) {
        temp?.activate(action)
    }

    open fun isBroken(): Boolean = false
}
