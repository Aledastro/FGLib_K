package com.uzery.fglib.utils

import com.uzery.fglib.utils.data.file.ConstL.LITTLE
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan

object MathUtils {
    fun mod(input: Double, mod: Double) = input%mod+(if (input < 0 && input%mod != 0.0) mod else 0.0)

    fun mod(input: Int, mod: Int) = input%mod+(if (input < 0 && input%mod != 0) mod else 0)

    fun roundL(input: Double, mod: Double) = input-mod(input, mod)
    fun roundL(input: Int, mod: Int) = input-mod(input, mod)

    fun roundC(input: Double, mod: Double) = roundL(input+mod/2, mod)

    fun roundC(input: Int, mod: Int) = roundL(input+mod/2, mod)

    fun roundR(input: Double, mod: Double) = if (mod(input, mod) == 0.0) roundL(input, mod)+1 else input

    fun roundR(input: Int, mod: Int) = if (mod(input, mod) == 0) roundL(input, mod)+1 else input


    fun min(vararg xs: Double) = xs.min()

    fun max(vararg xs: Double) = xs.max()

    fun getDegree(p: PointN): Double {
        var alpha = PI/2
        if (p.X != 0.0) alpha = atan(p.Y/p.X)
        if (p.X < 0.0 || p.X == 0.0 && p.Y < 0.0) alpha += PI
        return alpha
    }

    fun getDegree(c: PointN, p: PointN) = getDegree(p-c)

    fun little(input: Double) = abs(input) < LITTLE

    /*fun from10CC(n: Int, dim: Int, alphabet: String = ""): Int {
        val res = StringBuilder()
        var rest = n
        while (rest!=0) {
            val now = rest%dim
            res.append(now)
            rest/=dim
        }
        return res.toString().reversed()
    }

    fun to10CC(n: Int, dim: Int): Int {

    }

    fun CC(start: Int, start_dim: Int, end_dim: Int) {
        val n10 = to10CC(start, start_dim)
        return from10CC(n10, end_dim)
    }*/
}
