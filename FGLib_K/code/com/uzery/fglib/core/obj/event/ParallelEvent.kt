package com.uzery.fglib.core.obj.event

/**
 * [GameEvent], consisting of children [GameEvent]
 *
 * Children events running at the same time
 *
 * Ends when all children events are finished
 **/
open class ParallelEvent(vararg events: GameEvent): GameEvent() {

    private val events_list = ArrayList(events.toList())

    final override fun ready() = true

    final override fun ends() = events_list.all { it.wasReadyAndFinished() }

    fun add(vararg event: GameEvent) = events_list.addAll(event)

    init {
        onBirth {
            events_list.forEach { grab(it) }
        }
    }
}
