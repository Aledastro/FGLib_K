package com.uzery.fglib.extension.event

import com.uzery.fglib.utils.data.debug.DebugData
import java.util.*

abstract class CompositeGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()
    private var current: GameEvent? = null

    init {
        addListener { current?.activate(it) }
    }

    final override fun ready() = if(events.isNotEmpty()) events.first.wasReady() else {
        setValues()
        throw DebugData.error("events empty: $name")
    }

    final override fun update() {
        if(current == null || current!!.wasReadyAndEnds() && events.isNotEmpty()){
            current = events.removeFirst()
            produce(current!!)
            println(current!!.event_time)
        }
    }

    final override fun ends() = events.isEmpty() && (current?.wasReadyAndEnds() ?: false)

    protected fun add(event: GameEvent) = events.addLast(event)
}