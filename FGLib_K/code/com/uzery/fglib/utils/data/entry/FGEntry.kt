package com.uzery.fglib.utils.data.entry

/**
 * TODO("doc")
 **/
data class FGEntry(val name: String, val args: Array<Array<String>>) {
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

    override fun equals(other: Any?): Boolean {
        if (other !is FGEntry) return false

        return this.name == other.name && this.args.contentDeepEquals(other.args)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31*result+args.hashCode()
        return result
    }
}
