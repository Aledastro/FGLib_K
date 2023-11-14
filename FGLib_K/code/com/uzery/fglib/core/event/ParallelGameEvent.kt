package com.uzery.fglib.core.event

import java.util.*

open class ParallelGameEvent(vararg events: GameEvent): GameEvent() {

    private val events_list = LinkedList(events.toList())

    final override fun ready() = true

    final override fun ends() = events_list.all { it.wasReadyAndFinished() }

    fun add(event: GameEvent) = events_list.addLast(event)

    init {
        addAbility {
            if (object_time == 0) events_list.forEach { grab(it) }
        }
    }
}
