package com.uzery.fglib.core.event

import com.uzery.fglib.utils.data.debug.DebugData
import java.util.*

open class CompositeGameEvent(vararg events: GameEvent): GameEvent() {
    private var current: GameEvent? = null

    private val events_list = LinkedList(events.toList())

    init {
        addAbility {
            if (current == null && events_list.isEmpty()) {
                throw DebugData.error("ERROR: empty composite game event: $name")
            }

            if (event_time > 0 && (current == null || current!!.wasReadyAndFinished() && events_list.isNotEmpty())) {
                current = events_list.removeFirst()
                grab(current!!)
            }
        }
    }

    fun add(event: GameEvent) = events_list.addLast(event)


    final override fun ready() = true

    final override fun ends() = events_list.isEmpty() && (current?.wasReadyAndFinished() ?: false)
}
