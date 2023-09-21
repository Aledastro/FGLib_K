package com.uzery.fglib.core.event

import com.uzery.fglib.utils.data.debug.DebugData
import java.util.*

abstract class CompositeGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()
    private var current: GameEvent? = null

    init {
        addAbility {
            if(current == null && events.isEmpty()){
                setValues()
                throw DebugData.error("ERROR: empty composite game event: $name")
            }


            if (event_time > 0 && (current == null || current!!.wasReadyAndEnds() && events.isNotEmpty())) {
                current = events.removeFirst()
                produce(current!!)
            }
        }
    }

    final override fun ready() = true

    final override fun ends() = events.isEmpty() && (current?.wasReadyAndEnds() ?: false)

    override fun start() {}
    override fun update() {}
    override fun finish() {}

    protected fun add(event: GameEvent) = events.addLast(event)
}