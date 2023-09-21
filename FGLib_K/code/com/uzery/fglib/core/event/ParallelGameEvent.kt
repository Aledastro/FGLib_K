package com.uzery.fglib.core.event

import java.util.*

abstract class ParallelGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()

    final override fun ready() = true

    final override fun ends() = events.all { it.wasReadyAndEnds() }

    protected fun add(event: GameEvent) = events.addLast(event)

    final override fun start() {
        events.forEach { produce(it) }
    }

    override fun update() {}
    override fun finish() {}
}
