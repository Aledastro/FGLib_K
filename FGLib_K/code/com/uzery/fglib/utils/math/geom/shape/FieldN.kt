package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.matrix.UltraMatrix

data class FieldN(private val pos: PointN, private val normal: UltraMatrix){
    constructor(normal: UltraMatrix): this(PointN.ZERO, normal)
    constructor(pos: PointN, vararg normal: PointN): this(pos, UltraMatrix(*normal))


    val dim
        get() = normal.width
    val mirage
        get()=normal.copy(pos)

    fun copy(move: PointN) = FieldN(pos+move, normal)
    fun into(pos: PointN): Boolean {
        return mirage.into(pos)
    }

    fun intoHalf(pos: PointN): Boolean {
        return mirage.intoHalf(pos) //todo
    }

    operator fun times(other: FieldN): FieldN {
        return FieldN(mirage.connect(other.mirage))
    }

    override fun toString(): String {
        return "field[$pos, $normal]"
    }

    fun exists(): Boolean {
        return normal.exists()
    }

    operator fun unaryMinus(): FieldN {
        return FieldN(-mirage)
    }

    fun solve(): PointN? {
        mirage.solve()?.let { return PointN(it) }

        return null
    }
}
