package com.uzery.fglib.extension.event

import java.util.*

abstract class CompositeGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()
    private var current: GameEvent? = null

    final override fun ready() = events.first.ready()

    final override fun update() {
        if(current == null) current = events.removeFirst()
        current?.let {
            it.next()
            if(it.ends() && events.isNotEmpty()) current = events.removeFirst()
            children.addAll(it.children)
            it.children.clear()
        }
    }

    final override fun ends(): Boolean {
        return events.isEmpty() && current?.ends() ?: false
    }

    protected fun add(event: GameEvent) = events.addLast(event)
}