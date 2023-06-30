package com.uzery.fglib.extension.event

import java.util.*

abstract class CompositeGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()
    private var current: GameEvent? = null

    init {
        addListener { current?.activate(it) }
    }

    final override fun ready() = events.first.ready()

    private var is_current_ready = false

    final override fun update() {
        if(current == null) current = events.removeFirst()
        current?.let { event ->
            bounds = event.bounds
            event.next()
            children.addAll(event.children)
            event.children.clear()

            if(event.ready()) is_current_ready = true
            if(is_current_ready && event.ends() && events.isNotEmpty()) {
                current = events.removeFirst()
                is_current_ready = false
            }
        }
    }

    final override fun ends() = events.isEmpty() && is_current_ready && current?.ends() ?: false

    protected fun add(event: GameEvent) = events.addLast(event)
}