package com.uzery.fglib.utils.math

object TextUtils {
    fun splitText(text: String, width: Double, f: (String) -> Double): ArrayList<String> {
        val sep = listOf(' ', '\n')
        val res = ArrayList<String>()
        var now = ""
        var word = ""
        for (ch in text) {
            if (ch in sep) {
                if (f(now+word) > width || now.isEmpty()) {
                    res += now
                    now = ""
                }
                word = ""
            }
            word += ch
        }
        return res
    }

    fun splitText(text: String, width: Double): ArrayList<String> {
        return splitText(text, width) { it.length.toDouble() }
    }
}
