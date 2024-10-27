package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.FGUtils
import com.uzery.fglib.utils.data.debug.DebugData

object FGFormat {
    operator fun get(input: String): Pair<String, ArrayList<ArrayList<String>>> {
        val args = ArrayList<ArrayList<String>>()
        if (input.indexOf(':') == -1) return Pair(input, ArrayList())

        val name = FGUtils.subBefore(input, ":")
        val argsInput = FGUtils.subAfter(input, ":")

        val collector = StringBuilder()
        val list = ArrayList<String>()
        var special = false
        var adding = 0
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
                '[' -> {
                    adding++
                    if (adding > 1) {
                        collector.append(element)
                    }
                }

                ']' -> {
                    adding--
                    if (adding < 0) throw DebugData.error("Wrong FGFormat: $input")

                    if (adding != 0) {
                        collector.append(element)
                        continue
                    }

                    if (collector.isNotEmpty()) collect()
                    args.add(ArrayList(list))
                    list.clear()
                }

                ' ' -> {
                    if (adding > 0 && !waiting) {
                        collector.append(element)
                    } else continue
                }

                ',' -> {
                    if (adding == 0) throw DebugData.error("Wrong FGFormat: $input")
                    if (adding != 1) {
                        collector.append(element)
                        continue
                    }
                    if (collector.isNotEmpty()) collect()
                    waiting = true
                }

                else -> if (adding > 0) {
                    collector.append(element)
                    waiting = false
                }
            }
        }
        if (adding != 0) throw DebugData.error("Wrong FGFormat: $input")

        return Pair(name, args)
    }
}
