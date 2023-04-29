package com.uzery.fglib.core.obj

data class TagEffect(val name: String, val duration: Int) {
    private var time_left = duration
    var dead = false
        private set

    fun update() {
        time_left--
        if(time_left<0) dead = true
    }
}
