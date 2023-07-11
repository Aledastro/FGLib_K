package com.uzery.fglib.extension.event

import java.util.*

abstract class CompositeGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()
    private var current: GameEvent? = null

    init {
        addAbility{
            if(event_time>0 && (current==null || current!!.wasReadyAndEnds() && events.isNotEmpty())){
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