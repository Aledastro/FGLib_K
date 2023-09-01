package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.matrix.UltraMatrix

data class FieldN(private val pos: PointN, private val normal: UltraMatrix): Shape(){
    constructor(normal: UltraMatrix): this(PointN.ZERO, normal)

    private val mirage
        get()=normal.copy(pos)

    override fun copy(move: PointN) = FieldN(pos+move, normal)
    override fun into(pos: PointN): Boolean {
        return mirage.into(pos)
    }

    override val L
        get() = PointN(-100,-100) //todo: KOSTYL it wouldn't work for N-dim!!!
    override val R
        get() = PointN(100,100) //todo: KOSTYL it wouldn't work for N-dim!!!
    override val code = Code.FIELD

    operator fun times(other: FieldN): FieldN {
        return FieldN(mirage.connect(other.mirage))
    }

    override fun toString(): String {
        return "field[$pos, $normal]"
    }

    fun exists(): Boolean {
        return normal.exists()
    }
}
