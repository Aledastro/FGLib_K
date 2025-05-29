package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.MathUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.struct.num.IntI
import kotlin.math.PI
import kotlin.math.sign

/**
 * TODO("doc")
 **/
enum class Direct(private val x: Int, private val y: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0),
    UP_RIGHT(1, -1), UP_LEFT(-1, -1), DOWN_RIGHT(1, 1), DOWN_LEFT(-1, 1), CENTER(0, 0);

    val value = IntI(x, y)
    val valueP = PointN(value)
    val p = if (value == IntI()) PointN.ZERO else valueP/valueP.length()
    val vertical
        get() = this in VERTICAL
    val horizontal
        get() = this in HORIZONTAL

    fun alpha(): Double {
        return MathUtils.getDegree(p)
    }

    fun rotate45(steps: Int): Direct {
        if (this == CENTER) throw DebugData.error("Direct.CENTER can't be rotated")
        val id = ROTATE_ORDER.indexOf(this)+steps
        return ROTATE_ORDER[id.mod(ROTATE_ORDER.size)]
    }

    //todo test this
    fun rotate(alpha: Double) {
        val steps = ((alpha.mod(2*PI)+PI/8)/(PI/4)).toInt()
        rotate45(steps)
    }

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

        val VERTICAL = listOf(UP, DOWN)
        val HORIZONTAL = listOf(LEFT, RIGHT)

        val PRIMARY = listOf(UP, DOWN, LEFT, RIGHT)
        val DIAGONAL = listOf(UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)

        val ROTATE_ORDER = listOf(RIGHT, UP_RIGHT, UP, UP_LEFT, LEFT, DOWN_LEFT, DOWN, DOWN_RIGHT)
        val ROTATE_PRIMARY = listOf(RIGHT, UP, LEFT, DOWN)
        val ROTATE_DIAGONAL = listOf(UP_RIGHT, UP_LEFT, DOWN_LEFT, DOWN_RIGHT)
    }
}
