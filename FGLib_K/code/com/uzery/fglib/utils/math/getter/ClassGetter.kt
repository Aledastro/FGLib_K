package com.uzery.fglib.utils.math.getter

import com.uzery.fglib.utils.math.num.StringN

class ClassGetter<Type>(private val instance: ClassGetterInstance<Type>) {
    private fun getMark(input: String): () -> Type {
        val args = ArrayList<ArrayList<String>>()
        val startIndex = input.indexOf(':')
        if(startIndex == -1) return getMark(input, args)

        val name = input.substring(0, startIndex)
        val argsInput = input.substring(startIndex + 1)

        val collector = StringBuilder()
        val list = ArrayList<String>()
        var special = false
        var adding = false

        fun collect() {
            list.add(collector.toString())
            collector.clear()
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
                collector.append(element)
            }
            when(element) {
                '[' -> adding = true
                ']' -> {
                    if(collector.isNotEmpty()) collect()
                    args.add(ArrayList(list))
                    list.clear()
                    adding = false
                }

                ' ' -> continue
                ',' -> if(collector.isNotEmpty()) collect()

                else -> if(adding) collector.append(element)
            }
        }
        return getMark(name, args)
    }

    private fun getMark(name: String, args: ArrayList<ArrayList<String>>): () -> Type =
        instance.getMark(StringN(name, args.size), args)

    fun getFrom(name: String, args: ArrayList<ArrayList<String>>): Type = getMark(name, args).invoke()
    fun getFrom(input: String): Type = getMark(input).invoke()
    fun getEntry(id: Int) = instance.getEntry(id).invoke()
    fun getEntryName(id: Int) = instance.getEntryName(id)

    fun entry_size() = instance.entry_size()
}
