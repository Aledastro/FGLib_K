package com.uzery.fglib.utils

object SetUtils {
    fun <T> powerSet(vararg els: T): List<List<T>> {
        return powerSet(els.toList())
    }
    fun <T> powerSet(list: List<T>): List<List<T>> {
        val res = mutableListOf(emptyList<T>())
        for (element in list) {
            res += res.map { l -> l + element }
        }
        return res
    }
}
