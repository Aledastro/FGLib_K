package com.uzery.fglib.utils.math

class ArrayUtils {
    companion object {
        inline fun <reified T> toArray(vararg xs: T): Array<T> = Array(xs.size) {i->xs[i]}
        inline fun <reified T> transform(xs: Array<T>, transform: (x: T) -> T): Array<T> {
            val size = xs.size
            val result: Array<T> = Array(size) { i -> xs[i] }
            for(i in 0 until size) {
                result[i] = transform(xs[i])
            }
            return result
        }
        inline fun <reified T> transform(xs: Array<T>, ys: Array<T>, transform: (x: T, y: T) -> T): Array<T> {
            if(xs.size != ys.size) throw IllegalArgumentException("dim: ${xs.size}, ${ys.size}")
            val size = xs.size
            val result: Array<T> = Array(size) { i -> xs[i] }
            for(i in 0 until size) {
                result[i] = transform(xs[i], ys[i])
            }
            return result
        }
        inline fun <reified T> transformP(xs: Array<T>, ys: Array<T>, value: T,transform: (x: T, y: T) -> T): Array<T> {
            if(xs.size == ys.size) return transform(xs,ys,transform)
            if(xs.isNotEmpty() && ys.isNotEmpty()) throw IllegalArgumentException("dim: ${xs.size}, ${ys.size}")
            if(xs.isEmpty()) return transform(Array(ys.size){value},ys,transform)
            return transform(xs,Array(xs.size){value},transform)
        }
    }
}


