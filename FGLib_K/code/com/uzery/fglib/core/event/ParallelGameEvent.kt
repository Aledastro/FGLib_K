package com.uzery.fglib.core.event

open class ParallelGameEvent(vararg events: GameEvent): GameEvent() {

    private val events_list = ArrayList(events.toList())

    final override fun ready() = true

    final override fun ends() = events_list.all { it.wasReadyAndFinished() }

    fun add(event: GameEvent) = events_list.add(event)

    init {
        onBirth {
            events_list.forEach { grab(it) }
        }
    }
}
