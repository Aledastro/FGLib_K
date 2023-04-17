package com.uzery.fglib.utils.math.getter

import com.uzery.fglib.utils.math.num.StringN
import java.util.function.Supplier

class ClassGetter<Type>(private val instance: ClassGetterInstance<Type>) {
    fun getMark(input: String): Supplier<Type> {
        val args = ArrayList<ArrayList<String>>()
        val startIndex = input.indexOf(':')
        if(startIndex == -1) return getMark(input, args)

        val name = input.substring(0, startIndex)
        val argsInput = input.substring(startIndex + 1)

        var collector = ArrayList<Char>()
        val list = ArrayList<String>()
        var special = false
        var adding = false

        fun collect() {
            val s = StringBuilder()
            collector.forEach { c -> s.append(c) }
            list.add(s.toString())
            collector = ArrayList()
        }

        for(element in argsInput) {
            if(element == '{') {
                special = true
                continue
            }
            if(special) {
                if(element == '}') {
                    special = false
                    continue
                }
                collector.add(element)
            }
            when(element) {
                '[' -> adding = true
                ']' -> {
                    if(collector.isNotEmpty()) collect()
                    args.add(ArrayList(list))
                    list.clear()
                    adding = false
                }

                ' ' -> {}
                ',' -> if(collector.isNotEmpty()) collect()

                else -> if(adding) collector.add(element)
            }
        }
        return getMark(name, args)
    }

    fun getMark(name: String, args: ArrayList<ArrayList<String>>): Supplier<Type> =
        instance.getMark(StringN(name, args.size), args)

    fun getFrom(name: String, args: ArrayList<ArrayList<String>>): Type = getMark(name, args).get()
    fun getFrom(input: String): Type = getMark(input).get()
}