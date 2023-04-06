package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.math.ArrayUtils

data class PointN(val xs: Array<Double>) {
    constructor(vararg xs: Double): this(xs.toTypedArray())

    fun X(): Double {
        return xs[0]
    }

    fun Y(): Double {
        return xs[1]
    }

    fun Z(): Double {
        return xs[2]
    }


    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as PointN

        if(!xs.contentEquals(other.xs)) return false

        return true
    }

    override fun hashCode(): Int {
        return xs.contentHashCode()
    }

    operator fun plus(p: PointN): PointN {
        return PointN(ArrayUtils.transform(xs, p.xs) { x: Double, y: Double -> x + y })
    }

    operator fun minus(p: PointN): PointN {
        return PointN(ArrayUtils.transform(xs, p.xs) { x: Double, y: Double -> x - y })
    }
}
