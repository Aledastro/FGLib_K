package com.uzery.fglib.core.obj.event

import com.uzery.fglib.core.obj.GameObject

/**
 * [GameObject] with controlled live behavior
 *
 * 1) First it checks for `ready()`
 * 2) While it ready calls `start()`
 * 3) Then it loops with `update()`
 * 4) When it `ends()` calls `finish()`
 * 5) After `finish()` it `collapse()` or reinit if `repeatable`
 *
 * @property repeatable
 **/
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
                if (!ready()) return@addAbility

                start()
                init = true
            }
            update()
            if (ends()) {
                finish()
                if (repeatable) {
                    init = false
                    event_time = -1
                } else {
                    finished = true
                    collapse()
                }
            }
            event_time++
        }
    }
}
