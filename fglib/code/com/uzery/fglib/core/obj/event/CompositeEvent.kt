package com.uzery.fglib.core.obj.event

import com.uzery.fglib.utils.data.debug.DebugData
import java.util.*

/**
 * [GameEvent], consisting of children [GameEvent]
 *
 * Children events running one after another in list order
 *
 * Ends when all children events are finished
 **/
open class CompositeEvent(vararg events: GameEvent): GameEvent() {
    private var current: GameEvent? = null

    private val events_list = ArrayDeque(events.toList())

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

    fun add(vararg event: GameEvent) = events_list.addAll(event)


    final override fun ready() = true

    final override fun ends() = events_list.isEmpty() && (current?.wasReadyAndFinished() ?: false)
}
