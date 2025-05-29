package com.uzery.fglib.core.program.extension

abstract class GroupExtension(vararg ets: Extension): Extension(*ets) {
    final override fun init() {
        stats.toGroup()
        init0()
    }

    open fun init0() {}
}
