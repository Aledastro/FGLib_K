package com.uzery.fglib.ext.event

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