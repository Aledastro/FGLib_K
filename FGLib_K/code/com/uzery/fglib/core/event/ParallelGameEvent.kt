package com.uzery.fglib.core.event

import java.util.*

abstract class ParallelGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()

    init {
        addAbility {
            if (event_time == 0) events.forEach { produce(it) }
        }
    }

    final override fun ready() = true

    final override fun ends() = events.all { it.wasReadyAndEnds() }

    protected fun add(event: GameEvent) = events.addLast(event)

    override fun start() {}
    override fun update() {}
    override fun finish() {}
}