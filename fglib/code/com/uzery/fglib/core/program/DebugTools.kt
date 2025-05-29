package com.uzery.fglib.core.program

object DebugTools {
    val full_time = 0L

    val extension_time = HashMap<String, HashMap<String, Long>>()

    inline fun countTime(code: String, name: String, f: ()->Unit) {
        val t = System.nanoTime()
        f()
        val dt = System.nanoTime() - t

        val map = extension_time.getOrPut(code) { java.util.HashMap() }
        val et = map[name]
        map[name] = if (et == null) dt else et+dt
    }
}
