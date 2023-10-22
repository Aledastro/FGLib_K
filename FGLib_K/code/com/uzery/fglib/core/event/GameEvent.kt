package com.uzery.fglib.core.event

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox

abstract class GameEvent: GameObject() {
    var repeatable = false
        protected set

    var finished = false
        private set

    protected abstract fun ready(): Boolean
    protected open fun start() {}
    protected open fun update() {}
    protected open fun finish() {}
    protected abstract fun ends(): Boolean

    private var init = false

    fun wasReady(): Boolean {
        return init || ready()
    }

    fun wasReadyAndEnds(): Boolean {
        return wasReady() && ends()
    }

    var event_time = 0
        private set

    init {
        abilities.add(object: AbilityBox {
            override fun run() {
                if (!init) {
                    if (ready()) {
                        start()
                        init = true
                    } else return
                }
                update()
                if (ends()) {
                    finish()
                    if (repeatable) {
                        init = false
                    } else {
                        finished = true
                        collapse()
                    }
                }
                event_time++
            }
        })
    }
}
