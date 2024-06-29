package com.uzery.fglib.utils.struct

import com.uzery.fglib.utils.data.debug.DebugData

class RandomDrop<T>() {
    constructor(f: () -> T): this() {
        add(f)
    }

    private var full = -1.0
    private val list = ArrayList<() -> T>()
    private val priorities = ArrayList<Double>()
    fun add(priority: Double, f: () -> T) {
        list.add(f)
        priorities.add(priority)
    }

    fun add(f: () -> T) {
        list.add(f)
        priorities.add(1.0)
    }

    fun get(): T? {
        var size = priorities.sum()
        if (size == 0.0) return null

        if (full != -1.0) {
            if (full < size) throw DebugData.error("full is too small: SUM=$size / FULL=$full")
            size = full
        }
        return getWithSize(size)
    }

    fun getOrThrow(): T {
        return getWithSize(priorities.sum()) ?: throw DebugData.error("wrong: $priorities")
    }

    private fun getWithSize(size: Double): T? {
        val rand = Math.random()*size
        var p = 0.0
        var cycle = 0
        while (rand > p) {
            if (cycle >= list.size) return null
            p += priorities[cycle]
            cycle++
        }
        return list[cycle-1]()
    }

    fun setFull(full: Double) {
        this.full = full
    }
}
