package com.uzery.fglib.core.program.extension

internal data class ExtensionEntry(val e: Extension, val isAdd: Boolean) {
    companion object {
        fun ADD(e: Extension): ExtensionEntry {
            return ExtensionEntry(e, true)
        }
        fun REMOVE(e: Extension): ExtensionEntry {
            return ExtensionEntry(e, false)
        }
    }
}
