package com.uzery.fglib.utils

object ArrayUtils {
    inline fun <reified T> toArray(vararg xs: T): Array<T> = Array(xs.size) { i -> xs[i] }
    inline fun <reified T> transform(xs: Array<T>, transform: (x: T) -> T) =
        Array(xs.size) { i -> transform(xs[i]) }

    inline fun <reified T> transform(xs: Array<T>, ys: Array<T>, transform: (x: T, y: T) -> T): Array<T> {
        if (xs.size != ys.size) throw IllegalArgumentException("transform($xs $ys): dim: ${xs.size}, ${ys.size}")
        return Array(xs.size) { i -> transform(xs[i], ys[i]) }
    }

    //can work with zero arrays
    inline fun <reified T> transformP(
        xs: Array<T>,
        ys: Array<T>,
        value: T,
        transform: (x: T, y: T) -> T,
    ): Array<T> {
        if (xs.size == ys.size) return transform(xs, ys, transform)
        if (xs.isNotEmpty() && ys.isNotEmpty()) throw IllegalArgumentException("transformP(${xs.toList()} ${ys.toList()}): dim: ${xs.size}, ${ys.size}")
        if (xs.isEmpty()) return transform(Array(ys.size) { value }, ys, transform)
        return transform(xs, Array(xs.size) { value }, transform)
    }
}


