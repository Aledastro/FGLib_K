package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.math.FGUtils

object FGFormat {
    operator fun get(input: String): Pair<String, ArrayList<ArrayList<String>>> {
        val args = ArrayList<ArrayList<String>>()
        if (input.indexOf(':') == -1) return Pair(input, ArrayList())

        val name = FGUtils.subBefore(input, ":")
        val argsInput = FGUtils.subAfter(input, ":")

        val collector = StringBuilder()
        val list = ArrayList<String>()
        var special = false
        var adding = false
        var waiting = false

        fun collect() {
            list.add(collector.toString())
            collector.clear()
        }

        for (element in argsInput) {
            if (element == '{') {
                special = true
                continue
            }
            if (special) {
                if (element == '}') {
                    special = false
                    continue
                }
                collector.append(element)
            }
            when (element) {
                '[' -> adding = true
                ']' -> {
                    if (collector.isNotEmpty()) collect()
                    args.add(ArrayList(list))
                    list.clear()
                    adding = false
                }

                ' ' -> {
                    if (adding && !waiting) {
                        collector.append(element)
                    } else continue
                }

                ',' -> {
                    if (collector.isNotEmpty()) collect()
                    waiting = true
                }

                else -> if (adding) {
                    collector.append(element)
                    waiting = false
                }
            }
        }
        return Pair(name, args)
    }
}