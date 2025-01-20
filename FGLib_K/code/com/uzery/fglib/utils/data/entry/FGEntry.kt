package com.uzery.fglib.utils.data.entry

/**
 * TODO("doc")
 **/
data class FGEntry(val name: String, val args: ArrayList<ArrayList<String>>) {
    override fun toString(): String {
        return FGFormat.stringFrom(this)
    }
}
