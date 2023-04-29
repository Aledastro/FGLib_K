package com.uzery.fglib.utils.math.getter

import com.uzery.fglib.utils.data.debug.DebugData
import java.util.*

class Drop<T> {
    private var full = -1.0
    private val list = LinkedList<() -> T>()
    private val priorities = LinkedList<Double>()
    fun add(priority: Double, f: () -> T) {
        list.add(f)
        priorities.add(priority)
    }

    fun get(): T? {
        var size = priorities.sum()
        if(full != -1.0) {
            if(full<size) throw DebugData.error("full is too small: SUM=$size / FULL=$full")
            size = full
        }
        val rand = Math.random()*size
        var p = 0.0
        var cycle = 0
        while(rand>p) {
            if(cycle>=list.size) return null
            p += priorities[cycle]
            cycle++
        }
        return list[cycle - 1]()
    }

    fun setFull(full: Double) {
        this.full = full
    }
}