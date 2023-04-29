package com.uzery.fglib.core.obj

data class DrawLayer(var z: Double, var sort: Double = z) {
    companion object {
        val CAMERA_OFF = DrawLayer(0.0)
        val CAMERA_FOLLOW = DrawLayer(1.0)
    }
}
