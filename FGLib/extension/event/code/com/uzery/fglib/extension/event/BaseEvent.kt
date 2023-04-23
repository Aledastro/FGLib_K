package com.uzery.fglib.extension.event

import com.uzery.fglib.ext.event.GameEvent

abstract class BaseEvent: GameEvent() {
    override fun ready()= true

    override fun start() {
        /*ignore*/
    }

    override fun update() {
        /*ignore*/
    }

    override fun finish() {
        /*ignore*/
    }
}