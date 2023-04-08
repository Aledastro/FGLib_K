package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.math.ArrayUtils

data class PointN(private val xs: Array<Double>) {
    constructor(vararg xs: Double): this(xs.toTypedArray())
    constructor(p: PointN): this(Array(p.dimension()) { i -> p.xs[i] })

    fun get(n: Int): Double = xs[n]

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
        PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x: Double, y: Double -> x + y })

    operator fun minus(p: PointN): PointN =
        PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x: Double, y: Double -> x - y })

    operator fun div(p: PointN): PointN = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x: Double, y: Double -> x/y })
    operator fun div(v: Double): PointN = PointN(ArrayUtils.transform(xs) { x: Double -> x/v })
    operator fun div(v: Int): PointN = PointN(ArrayUtils.transform(xs) { x: Double -> x/v })

    operator fun times(p: PointN): PointN = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x: Double, y: Double -> x*y })
    operator fun times(v: Double): PointN = PointN(ArrayUtils.transform(xs) { x: Double -> x*v })
    operator fun times(v: Int): PointN = PointN(ArrayUtils.transform(xs) { x: Double -> x*v })
    fun dimension(): Int {
        return xs.size
    }


    companion object {
        val ZERO = PointN(Array(0) { 0.0 })
    }
}
