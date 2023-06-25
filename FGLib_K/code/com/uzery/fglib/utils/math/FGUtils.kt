package com.uzery.fglib.utils.math

import javafx.scene.paint.Color
import java.time.LocalDate
import java.time.LocalTime

interface FGUtils {
    companion object {
        fun hours(): Int = LocalTime.now().hour
        fun minutes(): Int = LocalTime.now().minute
        fun seconds(): Int = LocalTime.now().second

        fun time_YMD(): String {
            val date = LocalDate.now()
            return "${date.year}.${stain(date.month.ordinal + 1, 2)}.${stain(date.dayOfMonth, 2)}"
        }

        fun time_HMS(): String {
            return "${stain(hours()%24, 2)}:${stain(minutes()%60, 2)}:${stain(seconds()%60, 2)}"
        }

        fun stain(n: Int, rev: Int): String {
            val s = n.toString()
            if(s.length == rev) return s
            return if(s.length>rev) s.substring(s.length - rev) else "0".repeat(rev - s.length) + s
        }

        fun transparent(color: Color, k: Double): Color = Color.TRANSPARENT.interpolate(color, k)
        fun subBefore(input: String, index: String, delta: Int = 0): String {
            return input.substring(0 until input.indexOf(index) + delta)
        }

        fun subAfter(input: String, index: String, delta: Int = 0): String {
            return input.substring(input.indexOf(index) + 1 + delta)
        }
    }
}