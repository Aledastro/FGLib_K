package com.uzery.fglib.utils.math

class ArrayUtils {
    companion object {
        inline fun <reified T> transform(xs: Array<T>, ys: Array<T>, transform: (x: T, y: T) -> T): Array<T> {
            if(xs.size == ys.size) throw IllegalArgumentException("dim: ${xs.size}, ${ys.size}")
            val size = xs.size
            val result = Array(size) { i -> xs[i] }
            for(i in 0..size) {
                result[i] = transform(xs[i], ys[i])
            }
            return result
        }
    }
}


