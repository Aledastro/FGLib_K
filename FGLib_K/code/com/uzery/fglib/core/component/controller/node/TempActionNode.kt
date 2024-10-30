package com.uzery.fglib.core.component.controller.node

import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.controller.TempAction

abstract class TempActionNode {
    val action: () -> TempAction
        get() = { getAction() }

    var choose_ready = false
        private set

    val ready
        get() = isReady()

    val components = ArrayList<ObjectComponent>()

    fun add(vararg component: ObjectComponent) {
        components.addAll(component)
    }

    fun update() {
        choose_ready = isReady()
    }

    protected abstract fun getAction(): TempAction
    protected abstract fun isReady(): Boolean
}
