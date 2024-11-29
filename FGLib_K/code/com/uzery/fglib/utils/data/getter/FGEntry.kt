package com.uzery.fglib.utils.data.getter

data class FGEntry(val name: String, val args: ArrayList<ArrayList<String>>) {
    override fun toString(): String {
        return buildString {
            append(name)

            if (args.isNotEmpty()) {
                append(":")
                args.forEach { value ->
                    val s = value.toString()
                    append(if (s[s.lastIndex] == ']') " $s" else " [$s]")
                }
            }
        }
    }

    companion object {
        fun from(input: String) = FGFormat[input]
    }
}
