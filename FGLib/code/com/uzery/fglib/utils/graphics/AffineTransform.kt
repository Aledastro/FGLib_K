package com.uzery.fglib.utils.graphics

import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
class AffineTransform(val transform: (PointN) -> PointN, val t_size: (PointN) -> PointN) {
    fun pos(pos: PointN): PointN {
        return transform(pos)
    }

    fun size(pos: PointN, size: PointN): PointN {
        return transform(pos+size)-transform(pos)
    }

    operator fun times(other: AffineTransform): AffineTransform {
        return AffineTransform({ p -> this.transform(other.transform(p)) }, { p -> this.t_size(other.t_size(p)) })
    }

    companion object {
        val NEUTRAL: AffineTransform = AffineTransform({ p -> p }, { p -> p })
    }
}
