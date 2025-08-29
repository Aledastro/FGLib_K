package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.ArrayUtils
import com.uzery.fglib.utils.MathUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.struct.num.IntI
import kotlin.math.*

/**
 * TODO("doc")
 **/
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

    operator fun set(n: Int, value: Double) {
        if (dim == 0) return
        xs[n] = value
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

    val intX: Int
        get() = X.toInt()
    val intY: Int
        get() = Y.toInt()
    val intZ: Int
        get() = Z.toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PointN

        return xs.contentEquals(other.xs)
    }

    fun transform(transform: (x: Double) -> Double): PointN {
        return PointN(ArrayUtils.transform(xs, transform))
    }

    override fun hashCode(): Int = xs.contentHashCode()

    operator fun plus(p: PointN) = transform(this, p) { x, y -> x+y }
    operator fun minus(p: PointN) = transform(this, p) { x, y -> x-y }

    operator fun div(p: PointN) = transform(this, p) { x, y -> x/y }
    operator fun div(v: Double) = transform { x -> x/v }
    operator fun div(v: Int) = transform { x -> x/v }

    operator fun times(p: PointN) = transform(this, p) { x, y -> x*y }
    operator fun times(v: Double) = transform { x -> x*v }
    operator fun times(v: Int) = transform { x -> x*v }

    operator fun unaryMinus() = transform { x -> -x }

    fun less(other: PointN) = (this-other).isNegative()
    fun more(other: PointN) = (other-this).isNegative()
    private fun isNegative() = xs.all { i -> i <= 0 }

    fun lengthX2() = xs.sumOf { x -> x*x }
    fun length() = sqrt(lengthX2())

    fun normalised(): PointN {
        val len = length()
        return if (len == 0.0) PointN(this) else this/len
    }

    fun lengthTo(pos: PointN) = (this-pos).length()

    fun separate(level: Int) = PointN(Array(dim) { i -> if (level == i) xs[i] else 0.0 })

    fun roundL(size: Double) = transform { x -> MathUtils.roundL(x, size) }
    fun roundC(size: Double) = transform { x -> MathUtils.roundC(x, size) }
    fun roundR(size: Double) = transform { x -> MathUtils.roundR(x, size) }
    fun roundL(size: Int) = roundL(size.toDouble())
    fun roundC(size: Int) = roundC(size.toDouble())
    fun roundR(size: Int) = roundR(size.toDouble())

    fun mod(size: Double) = transform { x -> MathUtils.mod(x, size) }
    fun mod(size: Int) = mod(size.toDouble())
    fun mod(p: PointN) = transform(this, p) { x, y -> x.mod(y) }

    fun interpolate(pos: PointN, k: Double): PointN {
        return this+(pos-this)*k
    }

    fun coerceIn(posL: PointN, posR: PointN) = PointN(Array(dim) { i ->
        xs[i].coerceIn(min(posL[i], posR[i]), max(posL[i], posR[i]))
    })

    fun coerceL(posL: PointN) = PointN(Array(dim) { i ->
        xs[i].coerceAtLeast(posL[i])
    })

    fun coerceR(posL: PointN) = PointN(Array(dim) { i ->
        xs[i].coerceAtMost(posL[i])
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
        return "PointN${Array(xs.size) { i -> xs[i].toInt() }.toList()}"
    }

    companion object {
        val ZERO = PointN(Array(0) { 0.0 })
        val ZERO_1D = PointN(0)
        val ZERO_2D = PointN(0, 0)
        val ZERO_3D = PointN(0, 0, 0)

        fun transform(p1: PointN, p2: PointN, transform: (x: Double, y: Double) -> Double): PointN {
            return PointN(ArrayUtils.transformWith(p1.xs, p2.xs, 0.0, transform))
        }

        fun isSameDirection(p1: PointN, p2: PointN): Boolean {
            if (p1.dim != p2.dim) throw DebugData.error("WRONG DIM: $p1, $p2")
            var k: Double? = null
            for (i in 0..<p1.dim) {
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
