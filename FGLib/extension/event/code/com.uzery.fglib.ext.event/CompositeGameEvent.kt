package com.uzery.fglib.ext.event

import java.util.*

abstract class CompositeGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()
    private var current: GameEvent? = null

    final override fun ready() = events.first.ready()

    final override fun update() {
        if(current == null) current = events.removeFirst()
        val e = current ?: throw IllegalArgumentException()
        e.next()
        if(e.ends() && events.isNotEmpty()) current = events.removeFirst()

        children.addAll(e.children)
        e.children.clear()
    }

    final override fun ends(): Boolean {
        return events.isEmpty() && current?.ends() ?: false
    }

    protected fun add(event: GameEvent) = events.addLast(event)
}