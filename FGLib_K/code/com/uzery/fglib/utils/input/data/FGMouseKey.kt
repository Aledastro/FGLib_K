package com.uzery.fglib.utils.input.data

import javafx.scene.input.MouseButton

data class FGMouseKey(val value: String, val id: Int) {
    companion object {
        val PRIMARY = from(MouseButton.PRIMARY)
        val SECONDARY = from(MouseButton.SECONDARY)
        val MIDDLE = from(MouseButton.MIDDLE)
        val BACK = from(MouseButton.BACK)
        val FORWARD = from(MouseButton.FORWARD)
        val NONE = from(MouseButton.NONE)

        val LEFT
            get() = PRIMARY
        val RIGHT
            get() = SECONDARY

        fun from(key: MouseButton): FGMouseKey {
            return FGMouseKey(key.name, key.ordinal)
        }

        fun values(): Array<FGMouseKey> {
            return Array(MouseButton.values().size) { from(MouseButton.values()[it]) }
        }
    }
}
