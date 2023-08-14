package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
import kotlin.math.sign

enum class Direct(private val x: Int, private val y: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0),
    UP_RIGHT(1, -1), UP_LEFT(-1, -1), DOWN_RIGHT(1, 1), DOWN_LEFT(-1, 1), CENTER(0, 0);

    val value = PointN(x.toDouble(), y.toDouble())
    val p = if(value.length()==0.0) PointN.ZERO else value/value.length()

    operator fun plus(dir: Direct) = from(x+dir.x, y+dir.y)

    operator fun minus(dir: Direct) = from(x-dir.x, y-dir.y)

    operator fun unaryMinus() = from(-x, -y)

    companion object {
        fun from(x: Int, y: Int): Direct {
            when (IntI(x.coerceIn(-1, 1), y.coerceIn(-1, 1))) {
                IntI(-1, -1) -> return UP_LEFT
                IntI(0, -1) -> return UP
                IntI(1, -1) -> return UP_RIGHT
                IntI(-1, 0) -> return LEFT
                IntI(0, 0) -> return CENTER
                IntI(1, 0) -> return RIGHT
                IntI(-1, 1) -> return DOWN_LEFT
                IntI(0, 1) -> return DOWN
                IntI(1, 1) -> return DOWN_RIGHT
            }
            throw DebugData.error("from: $x $y")
        }

        fun from(x: Double, y: Double): Direct {
            return from(x.sign.toInt(), y.sign.toInt())
        }
    }
}