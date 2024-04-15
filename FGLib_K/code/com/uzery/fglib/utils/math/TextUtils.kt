package com.uzery.fglib.utils.math

object TextUtils {
    fun splitText(text: String, width: Double, f: (String) -> Double = default_f): ArrayList<String> {
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

    fun splitTextAndMerge(text: String, width: Double, f: (String) -> Double = default_f): String {
        val data = splitText(text, width, f)
        var res = ""
        for (s in data){
            res += s+"\n"
        }
        return res
    }

    private val default_f: (String)->Double = { it.length.toDouble() }
}
