package com.uzery.fglib.utils

import com.uzery.fglib.utils.SplitUtils.unite

/**
 * TODO("doc")
 **/
object StringUtils {
    fun applyToEach(input: String, del: String, action: (String) -> String): String {
        val keep_first = input.startsWith(del)
        val keep_last = input.endsWith(del)

        val dat = input.split(del)
        val res = ArrayList<String>()
        dat.forEach { res.add(action(it)) }

        return unite(res, del, keep_first, keep_last)
    }

    fun applyToEach(input: String, action: (String) -> String) = applyToEach(input, "\n", action)
}
