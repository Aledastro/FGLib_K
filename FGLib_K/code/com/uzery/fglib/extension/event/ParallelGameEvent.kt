package com.uzery.fglib.extension.event

import com.uzery.fglib.utils.data.debug.DebugData
import java.util.*

abstract class ParallelGameEvent: GameEvent() {
    private val events = LinkedList<GameEvent>()

    init{
        addAbility{
            if(event_time==0) events.forEach { produce(it) }
        }
    }

    final override fun ready() = true

    final override fun ends() = events.all { it.wasReadyAndEnds() }

    protected fun add(event: GameEvent) = events.addLast(event)

    override fun start() {}
    override fun update() {}
    override fun finish() {}
}