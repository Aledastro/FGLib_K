package com.uzery.fglib.utils.input

abstract class KeyActivator<Key>(private val size: Int) {
    private var timePressed = Array(size) { 0L }
    private var timeReleased = Array(size) { 0L }
    private var lastTimePressed = Array(size) { 0L }
    private var lastTimeReleased = Array(size) { 0L }
    private var block = Array(size) { false }
    protected abstract fun pressed0(code: Int): Boolean

    protected abstract fun from(key: Key): Int

    /** extremely important function! **/
    fun update() {
        for (i in 0 until size) {
            timePressed[i]++
            timeReleased[i]++
            if (pressedInt(i)) {
                lastTimeReleased[i] = timeReleased[i]
                timeReleased[i] = 0
            } else {
                lastTimePressed[i] = timePressed[i]
                timePressed[i] = 0
            }
        }
    }

    private fun pressedInt(code: Int): Boolean = !block[code] && pressed0(code)


    //////////////////////////////////////////////////////////////////////////////////////////

    fun refuseButtons() {
        timePressed.fill(0)
        timeReleased.fill(Long.MAX_VALUE/2)
        lastTimePressed.fill(0)
        lastTimeReleased.fill(Long.MAX_VALUE/2)
    }

    fun timePressed(key: Key) = timePressed[from(key)]
    fun lastTimePressed(key: Key) = lastTimePressed[from(key)]
    fun timeReleased(key: Key) = timeReleased[from(key)]
    fun lastTimeReleased(key: Key) = lastTimeReleased[from(key)]
    fun block(vararg keys: Key) = keys.forEach { key -> block[from(key)] = true }
    fun unblock(vararg keys: Key) = keys.forEach { key -> block[from(key)] = false }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    fun pressed(key: Key) = pressedInt(from(key))
    fun pressedIn(frames: Int, key: Key) = timeReleased(key) < frames || pressed(key)
    fun inPressedIn(frames: Int, key: Key) = timePressed(key) in 1..frames
    fun rePressedIn(frames: Int, key: Key) = timeReleased(key) in 1..frames
    fun inPressed(key: Key) = inPressedIn(1, key)
    fun rePressed(key: Key) = rePressedIn(1, key)

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    fun anyPressed(vararg keys: Key) = keys.any { key -> pressed(key) }
    fun allPressed(vararg keys: Key) = keys.all { key -> pressed(key) }

    fun anyInPressed(vararg keys: Key) = keys.any { key -> inPressed(key) }
    fun allInPressed(vararg keys: Key) = keys.all { key -> inPressed(key) }

    fun anyRePressed(vararg keys: Key) = keys.any { key -> rePressed(key) }
    fun allRePressed(vararg keys: Key) = keys.all { key -> rePressed(key) }

    fun anyPressedIn(frames: Int, vararg keys: Key) = keys.any { key -> pressedIn(frames, key) }
    fun allPressedIn(frames: Int, vararg keys: Key) = keys.all { key -> pressedIn(frames, key) }

    fun anyInPressedIn(frames: Int, vararg keys: Key) = keys.any { key -> inPressedIn(frames, key) }
    fun allInPressedIn(frames: Int, vararg keys: Key) = keys.all { key -> inPressedIn(frames, key) }

    fun anyRePressedIn(frames: Int, vararg keys: Key) = keys.any { key -> rePressedIn(frames, key) }
    fun allRePressedIn(frames: Int, vararg keys: Key) = keys.all { key -> rePressedIn(frames, key) }

    ///////////////////////////////////////////////////////////////////////////////////////
}