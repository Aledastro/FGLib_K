package com.uzery.fglib.utils.input.data

import javafx.scene.input.MouseButton

data class FGMouseKey(val value: String, val id: Int) {


    companion object {
        val LEFT = from(MouseButton.PRIMARY)
        val RIGHT = from(MouseButton.SECONDARY)
        val MIDDLE = from(MouseButton.MIDDLE)
        val BACK = from(MouseButton.BACK)
        val FORWARD = from(MouseButton.FORWARD)
        val NONE = from(MouseButton.NONE)

        private fun from(key: MouseButton): FGMouseKey {
            return FGMouseKey(key.name, key.ordinal)
        }
    }
}
