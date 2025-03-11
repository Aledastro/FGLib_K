package com.uzery.fglib.core.component.controller

import com.uzery.fglib.core.component.listener.InputAction

/**
 * Basic temporary action for [Controller]
 *
 * Updates every frame, also reacts on [InputAction] activation
 *
 * After action ends [Controller] chooses another [InputAction]
 *
 * @property temp_time
 **/
abstract class TempAction {
    protected var temp_time = 0
        private set
    internal var finished = false
        private set

    internal fun next() {
        if (temp_time == 0) start()

        update()

        if (ends()) {
            onSuccessFinish()
            finish()
            finished = true
        }
        temp_time++
    }

    internal fun breakFinish() {
        onBrokenFinish()
        finish()
        finished = true
    }

    open fun start() {}
    open fun update() {}
    open fun finish() {}
    open fun onSuccessFinish() {}
    open fun onBrokenFinish() {}

    open fun activate(action: InputAction) {}

    abstract fun ends(): Boolean
}
