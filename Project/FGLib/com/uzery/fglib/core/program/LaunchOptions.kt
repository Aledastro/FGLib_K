package com.uzery.fglib.core.program

data class LaunchOptions(val width: Double, val height: Double) {
    companion object {
        val default = LaunchOptions(700.0, 700.0)
    }
}
