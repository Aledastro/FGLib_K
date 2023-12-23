package com.uzery.fglib.utils.math

import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.math.geom.PointN
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

object FGUtils {
    val project_dir: String = File("").absolutePath

    fun hours(): Int = LocalTime.now().hour
    fun minutes(): Int = LocalTime.now().minute
    fun seconds(): Int = LocalTime.now().second

    fun isComment(s: String) = s.isEmpty() || s.startsWith("//")

    fun time_YMD(): String {
        val date = LocalDate.now()
        return "${date.year}.${stain(date.month.ordinal+1, 2)}.${stain(date.dayOfMonth, 2)}"
    }

    fun time_HMS(): String {
        return "${stain(hours()%24, 2)}:${stain(minutes()%60, 2)}:${stain(seconds()%60, 2)}"
    }

    fun stain(n: Int, rev: Int): String {
        val s = n.toString()
        if (s.length == rev) return s
        return if (s.length > rev) s.substring(s.length-rev) else "0".repeat(rev-s.length)+s
    }

    fun subBefore(input: String, index: String, delta: Int = 0): String {
        return input.substring(0 until input.indexOf(index)+delta)
    }

    fun subBeforeLast(input: String, index: String, delta: Int = 0): String {
        return input.substring(0 until input.lastIndexOf(index)+delta)
    }

    fun subAfter(input: String, index: String, delta: Int = 0): String {
        return input.substring(input.indexOf(index)+1+delta)
    }

    fun subAfterLast(input: String, index: String, delta: Int = 0): String {
        return input.substring(input.lastIndexOf(index)+1+delta)
    }

    fun getPosFrom(s: String): PointN {
        val c = object: ClassGetter<PointN>() {
            override fun addAll() = add("pos", 1) { pos }
        }
        return c["pos: $s"]
    }
}