package com.uzery.fglib.core.program.extension

data class UILevel(val level: Double) {
    companion object {
        val NEUTRAL = UILevel(0.0)
        val UP = UILevel(1.0)
        val DOWN = UILevel(-1.0)
    }
}
