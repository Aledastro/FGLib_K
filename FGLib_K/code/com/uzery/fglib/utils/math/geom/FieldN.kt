package com.uzery.fglib.utils.math.geom

import com.uzery.fglib.utils.math.matrix.UltraMatrix

data class FieldN(private val pos: PointN, val normal: UltraMatrix) {
    constructor(normal: UltraMatrix): this(PointN.ZERO, normal)
    constructor(pos: PointN, vararg normal: PointN): this(pos, UltraMatrix(*normal))

    val dim
        get() = normal.width

    val mirage = normal.copy(pos)

    fun copy(move: PointN) = FieldN(pos+move, normal.copyU())
    fun into(pos: PointN): Boolean {
        return normal.into(pos-this.pos)
    }

    fun intoS(pos: PointN): Boolean {
        return normal.intoS(pos-this.pos)
    }

    fun intoHalf(pos: PointN): Boolean {
        return normal.intoHalf(pos-this.pos)
    }

    fun intoHalfS(pos: PointN, value: Double = 1.0): Boolean {
        return normal.intoHalfS(pos-this.pos, value)
    }

    operator fun times(other: FieldN): FieldN {
        return FieldN(mirage.connect(other.mirage))
    }

    override fun toString(): String {
        return "field[$pos, $normal, ${mirage.sign.toList()}]"
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
