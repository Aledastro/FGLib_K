package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.ArrayUtils
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.num.IntI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

data class PointN(private val xs: Array<Double>) {
    val dim
        get() = xs.size

    constructor(vararg xs: Double): this(Array<Double>(xs.size) { i -> xs[i] })
    constructor(vararg xs: Int): this(Array<Double>(xs.size) { i -> xs[i].toDouble() })
    constructor(p: PointN): this(Array(p.dim) { i -> p.xs[i] })

    constructor(p: IntI): this(p.x, p.y)

    operator fun get(n: Int): Double {
        if (dim == 0) return 0.0
        return xs[n]
    }

    var X: Double
        get() = get(0)
        set(value) {
            xs[0] = value
        }
    var Y: Double
        get() = get(1)
        set(value) {
            xs[1] = value
        }
    var Z: Double
        get() = get(2)
        set(value) {
            xs[2] = value
        }
    val XP: PointN
        get() = separate(0)
    val YP: PointN
        get() = separate(1)
    val ZP: PointN
        get() = separate(2)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointN

        if (!xs.contentEquals(other.xs)) return false

        return true
    }

    override fun hashCode(): Int = xs.contentHashCode()

    operator fun plus(p: PointN) = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x+y })

    operator fun minus(p: PointN) = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x-y })

    operator fun div(p: PointN) = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x/y })
    operator fun div(v: Double) = PointN(ArrayUtils.transform(xs) { x -> x/v })
    operator fun div(v: Int) = PointN(ArrayUtils.transform(xs) { x -> x/v })

    operator fun times(p: PointN) = PointN(ArrayUtils.transformP(xs, p.xs, 0.0) { x, y -> x*y })
    operator fun times(v: Double) = PointN(ArrayUtils.transform(xs) { x -> x*v })
    operator fun times(v: Int) = PointN(ArrayUtils.transform(xs) { x -> x*v })

    operator fun unaryMinus() = PointN(ArrayUtils.transform(xs) { x -> -x })

    fun less(other: PointN) = (this-other).isNegative()
    fun more(other: PointN) = (other-this).isNegative()

    private fun isNegative() = xs.all { i -> i <= 0 }
    fun lengthX2(): Double {
        return xs.sumOf { x -> x*x }
    }

    fun length() = sqrt(lengthX2())

    fun lengthTo(pos: PointN) = (this-pos).length()

    fun separate(level: Int) = PointN(Array(dim) { i -> if (level == i) xs[i] else 0.0 })

    fun transform(transform: (x: Double) -> Double) = PointN(ArrayUtils.transform(xs, transform))
    fun roundL(size: Double) = transform { x -> MathUtils.roundL(x, size) }
    fun roundC(size: Double) = transform { x -> MathUtils.roundC(x, size) }
    fun roundR(size: Double) = transform { x -> MathUtils.roundR(x, size) }
    fun mod(size: Double) = transform { x -> MathUtils.mod(x, size) }
    fun interpolate(pos: PointN, k: Double): PointN {
        return this+(pos-this)*k
    }

    fun coerceIn(posL: PointN, posR: PointN) = PointN(Array(dim) { i ->
        xs[i].coerceIn(posL[i], max(posL[i], posR[i]))
    })

    fun rotateXY(d: Double): PointN {
        val p = PointN(this)
        val alpha = MathUtils.getDegree(PointN(p.X, p.Y))+d
        val length = PointN(p.X, p.Y).length()
        p.X = cos(alpha)*length
        p.Y = sin(alpha)*length
        return p
    }

    fun rotateXZ(d: Double): PointN {
        val p = PointN(this)
        val alpha = MathUtils.getDegree(PointN(p.X, p.Z))+d
        val length = PointN(p.X, p.Z).length()
        p.X = cos(alpha)*length
        p.Z = sin(alpha)*length
        return p
    }

    fun rotateYZ(d: Double): PointN {
        val p = PointN(this)
        val alpha = MathUtils.getDegree(PointN(p.Y, p.Z))+d
        val length = PointN(p.Y, p.Z).length()
        p.Y = cos(alpha)*length
        p.Z = sin(alpha)*length
        return p
    }

    override fun toString(): String {
        return "PointN${Array(xs.size) { xs[it].toInt() }.toList()}"
    }

    companion object {
        val ZERO = PointN(Array(0) { 0.0 })

        fun transform(p1: PointN, p2: PointN, transform: (x: Double, y: Double) -> Double): PointN {
            return PointN(ArrayUtils.transformP(p1.xs, p2.xs, 0.0, transform))
        }

        fun isSameDirection(p1: PointN, p2: PointN): Boolean {
            if (p1.dim != p2.dim) throw DebugData.error("WRONG DIM: $p1, $p2")
            var k: Double? = null
            for (i in 0 until p1.dim) {
                if (p1.xs[i] == 0.0 && p2.xs[i] == 0.0) continue
                if (p1.xs[i] == 0.0 || p2.xs[i] == 0.0) return false
                val now_k = p1.xs[i]/p2.xs[i]
                if (k == null) k = now_k
                if (now_k != k) return false
            }
            return true
        }
    }
}
