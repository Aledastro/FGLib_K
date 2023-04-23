package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.math.ArrayUtils
import com.uzery.fglib.utils.math.MathUtils
import kotlin.math.sqrt

data class PointN(private val xs: Array<Double>) {
    constructor(vararg xs: Double): this(xs.toTypedArray())
    constructor(p: PointN): this(Array(p.dimension()) { i -> p.xs[i] })

    operator fun get(n: Int): Double {
        if(dimension() == 0) return 0.0;
        return xs[n]
    }

    fun X(): Double = get(0)
    fun Y(): Double = get(1)
    fun Z(): Double = get(2)


    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as PointN

        if(!xs.contentEquals(other.xs)) return false

        return true
    }

    override fun hashCode(): Int = xs.contentHashCode()

    operator fun plus(p: PointN): PointN =
        PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x + y })

    operator fun minus(p: PointN): PointN =
        PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x - y })

    operator fun div(p: PointN) = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x/y })
    operator fun div(v: Double) = PointN(ArrayUtils.transform(xs) { x -> x/v })
    operator fun div(v: Int) = PointN(ArrayUtils.transform(xs) { x -> x/v })

    operator fun times(p: PointN) = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x*y })
    operator fun times(v: Double) = PointN(ArrayUtils.transform(xs) { x -> x*v })
    operator fun times(v: Int) = PointN(ArrayUtils.transform(xs) { x -> x*v })

    operator fun unaryMinus() = PointN(ArrayUtils.transform(xs) { x -> -x })
    fun dimension() = xs.size
    fun less(other: PointN) = (this - other).isNegative()
    fun more(other: PointN) = (other - this).isNegative()

    private fun isNegative() = xs.all { i -> i<=0 }
    fun lengthX2(): Double {
        var sum = 0.0
        xs.forEach { x -> sum += x*x }
        return sum
    }

    fun length() = sqrt(lengthX2())
    fun separate(level: Int) = PointN(Array(dimension()) { i -> if(level == i) xs[i] else 0.0 })

    fun transform(transform: (x: Double) -> Double) = PointN(ArrayUtils.transform(xs, transform))
    fun round(size: Double) = transform { x -> x - MathUtils.mod(x, size) }

    companion object {
        val ZERO = PointN(Array(0) { 0.0 })

        fun transform(p1: PointN, p2: PointN, transform: (x: Double, y: Double) -> Double): PointN {
            return PointN(ArrayUtils.transformP(p1.xs, p2.xs, 0.0, transform))
        }
    }
}
