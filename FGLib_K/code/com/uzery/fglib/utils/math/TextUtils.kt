package com.uzery.fglib.utils.math

object TextUtils {
    fun splitText(text: String, width: Double, f: (String) -> Double): ArrayList<String> {
        val sep = listOf(' ', '\n')
        val res = ArrayList<String>()
        var now = ""
        var word = ""

        fun endLine(){
            if (now.isEmpty()) return

            res.add(now)
            now = ""
        }

        fun endWord(){
            if (f(now+word) > width || now.isEmpty()) {
                endLine()
            }

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

    fun splitText(text: String, width: Double): ArrayList<String> {
        return splitText(text, width) { it.length.toDouble() }
    }
}
