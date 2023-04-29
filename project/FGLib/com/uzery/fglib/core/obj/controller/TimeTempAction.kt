package com.uzery.fglib.core.obj.controller

abstract class TimeTempAction: TempAction {
    protected var ids_time = 0
    private var finished = false

    override fun next() {
        if(ids_time == 0) start()

        update()

        if(ends()) {
            finish()
            finished = true
        }
        ids_time++
    }

    open fun start() {
        /*ignore*/
    }

    open fun update() {
        /*ignore*/
    }

    open fun finish() {
        /*ignore*/
    }

    abstract fun ends(): Boolean

    override val ends: Boolean
        get() = finished
}
