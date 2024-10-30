package com.uzery.fglib.core.obj.event

import com.uzery.fglib.core.obj.GameObject

abstract class GameEvent: GameObject() {
    var repeatable = false
        protected set

    var finished = false
        private set

    protected open fun ready(): Boolean = true
    protected open fun start() {}
    protected open fun update() {}
    protected open fun finish() {}
    protected open fun ends(): Boolean = true

    private var init = false

    fun wasReady(): Boolean {
        return init || ready()
    }

    fun wasReadyAndFinished(): Boolean {
        return wasReady() && finished
    }

    var event_time = 0
        private set

    init {
        addAbility {
            if (!init) {
                if (ready()) {
                    start()
                    init = true
                } else return@addAbility
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
    }
}
