package com.uzery.fglib.utils

object StringUtils {
    fun re_split(dat: List<String>, del: String, keep_first: Boolean = false, keep_last: Boolean = true): String {
        var res = ""
        dat.forEach { res += it+del }

        if (keep_first) res = del+res
        if (!keep_last) res = res.substring(0, res.length-del.length)

        return res
    }
    fun re_split(dat: Array<String>, del: String, keep_first: Boolean = false, keep_last: Boolean = true): String {
        return re_split(dat.toList(), del, keep_first, keep_last)
    }

    fun applyToEach(input: String, del: String, action: (String) -> String): String {
        val keep_first = input.startsWith(del)
        val keep_last = input.endsWith(del)

        val dat = input.split(del)
        val res = ArrayList<String>()
        dat.forEach { res.add(action(it)) }

        return re_split(res, del, keep_first, keep_last)
    }

    fun applyToEach(input: String, action: (String) -> String) = applyToEach(input, "\n", action)
}
