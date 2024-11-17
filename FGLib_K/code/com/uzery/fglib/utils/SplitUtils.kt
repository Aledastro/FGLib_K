package com.uzery.fglib.utils

object SplitUtils {
    fun splitTextByWidth(text: String, width: Double, f: (String) -> Double = { it.length.toDouble() }): ArrayList<String> {
        val sep = listOf(' ', '\n')
        val res = ArrayList<String>()
        var now = ""
        var word = ""

        fun endLine() {
            if (now.isEmpty()) return

            res.add(now)
            now = ""
        }

        fun endWord() {
            if (f(now+word) > width || now.isEmpty()) endLine()

            now += word
            word = ""
        }

        for (ch in text) {
            word += ch

            if (ch in sep) endWord()
            if (ch == '\n') endLine()
        }
        endWord()
        endLine()

        return res
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun unite(dat: List<String>, del: String, keep_first: Boolean = false, keep_last: Boolean = true): String {
        return buildString {
            if (keep_first) append(del)

            dat.forEach { append(it+del) }

            if (!keep_last) substring(0, length-del.length)
        }
    }

    fun unite(dat: Array<String>, del: String, keep_first: Boolean = false, keep_last: Boolean = true): String {
        return unite(dat.toList(), del, keep_first, keep_last)
    }
}
