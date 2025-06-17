package com.uzery.fglib.utils

import com.uzery.fglib.utils.SplitUtils.unite

/**
 * TODO("doc")
 **/
object StringUtils {
    fun applyToEach(input: String, del: String, action: (String) -> String): String {
        val keep_first = input.startsWith(del)
        val keep_last = input.endsWith(del)

        val dat = input
            .split(del)
            .map { action(it) }

        return unite(dat, del, keep_first, keep_last)
    }

    fun applyToEach(input: String, action: (String) -> String) = applyToEach(input, "\n", action)
}
