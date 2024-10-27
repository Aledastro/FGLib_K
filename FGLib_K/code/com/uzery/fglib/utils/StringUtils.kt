package com.uzery.fglib.utils

object StringUtils {
    fun re_split(dat: List<String>, del: String, keep_first: Boolean = false, keep_last: Boolean = true): String {
        return buildString {
            if (keep_first) append(del)

            dat.forEach { append(it+del) }

            if (!keep_last) substring(0, length-del.length)
        }
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
