package com.uzery.fglib.utils.input

abstract class KeyActivator<Key>(private val size: Int) {
    private var timePressed = Array(size) { 0 }
    private var timeReleased = Array(size) { 0 }
    private var lastTimePressed = Array(size) { 0 }
    private var lastTimeReleased = Array(size) { 0 }
    private var blocked = Array(size) { false }
    protected abstract fun pressed0(code: Int): Boolean

    protected abstract fun from(key: Key): Int

    /** extremely important function! **/
    fun update() {
        for(i in 0 until size) {
            timePressed[i]++
            timeReleased[i]++
            if(pressedInt(i)) {
                lastTimeReleased[i] = timeReleased[i]
                timeReleased[i] = 0
            } else {
                lastTimePressed[i] = timePressed[i]
                timePressed[i] = 0
            }
        }
    }

    private fun pressedInt(code: Int): Boolean = !blocked[code] && pressed0(code)


    //////////////////////////////////////////////////////////////////////////////////////////

    fun pressed(key: Key): Boolean = pressedInt(from(key))

}