package com.uzery.fglib.utils.input.data

enum class FGMouseKey {
    NONE,
    PRIMARY,
    MIDDLE,
    SECONDARY,
    BACK,
    FORWARD;

    val LEFT
        get() = PRIMARY
    val RIGHT
        get() = SECONDARY
}
