package com.uzery.fglib.utils.input

abstract class KeyActivator<Key>(private val values: Array<Key>) {
    private val size = values.size

    private var pressedInt = Array(size) { false }
    private var timePressed = Array(size) { 0L }
    private var timeReleased = Array(size) { 0L }
    private var lastTimePressed = Array(size) { 0L }
    private var lastTimeReleased = Array(size) { 0L }
    private var block = Array(size) { false }

    protected abstract fun pressed0(code: Int): Boolean

    protected abstract fun fromKey(key: Key): Int

    /** extremely important function! **/
    fun update() {
        for (i in 0 until size) {
            pressedInt[i] = pressedInt(i)
            timePressed[i]++
            timeReleased[i]++
            if (pressedInt[i]) {
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

    fun timePressed(key: Key) = timePressed[fromKey(key)]
    fun lastTimePressed(key: Key) = lastTimePressed[fromKey(key)]
    fun timeReleased(key: Key) = timeReleased[fromKey(key)]
    fun lastTimeReleased(key: Key) = lastTimeReleased[fromKey(key)]
    fun block(vararg keys: Key) = keys.forEach { key -> block[fromKey(key)] = true }
    fun unblock(vararg keys: Key) = keys.forEach { key -> block[fromKey(key)] = false }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    fun pressed(key: Key) = pressedInt[fromKey(key)]
    fun pressedIn(frames: Int, key: Key) = timeReleased(key) < frames || pressed(key)
    fun inPressedIn(frames: Int, key: Key) = timePressed(key) in 1..frames
    fun rePressedIn(frames: Int, key: Key) = timeReleased(key) in 1..frames
    fun inPressed(key: Key) = inPressedIn(1, key)
    fun rePressed(key: Key) = rePressedIn(1, key)

    ///////////////////////////////////////////////////////////////////////////////////////

    fun periodPressed(start_time: Int, period: Int, key: Key): Boolean {
        val time = timePressed(key)
        return inPressed(key) || time >= start_time && (time-start_time)%period == 0L
    }

    fun periodPressed(period: Int, key: Key) = periodPressed(period, period, key)

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun from(keys: Array<out Key>): Array<out Key> {
        return if (keys.isEmpty()) values else keys
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    fun anyPressed(vararg keys: Key) = from(keys).any { key -> pressed(key) }
    fun allPressed(vararg keys: Key) = from(keys).all { key -> pressed(key) }

    fun anyPressedIn(frames: Int, vararg keys: Key) = from(keys).any { key -> pressedIn(frames, key) }
    fun allPressedIn(frames: Int, vararg keys: Key) = from(keys).all { key -> pressedIn(frames, key) }

    fun anyInPressedIn(frames: Int, vararg keys: Key) = from(keys).any { key -> inPressedIn(frames, key) }
    fun allInPressedIn(frames: Int, vararg keys: Key) = from(keys).all { key -> inPressedIn(frames, key) }

    fun anyRePressedIn(frames: Int, vararg keys: Key) = from(keys).any { key -> rePressedIn(frames, key) }
    fun allRePressedIn(frames: Int, vararg keys: Key) = from(keys).all { key -> rePressedIn(frames, key) }

    fun anyInPressed(vararg keys: Key) = from(keys).any { key -> inPressed(key) }
    fun allInPressed(vararg keys: Key) = from(keys).all { key -> inPressed(key) }

    fun anyRePressed(vararg keys: Key) = from(keys).any { key -> rePressed(key) }
    fun allRePressed(vararg keys: Key) = from(keys).all { key -> rePressed(key) }

    ///////////////////////////////////////////////////////////////////////////////////////

    fun allPeriodPressed(start_time: Int, period: Int, vararg keys: Key) =
        from(keys).all { key -> periodPressed(start_time, period, key) }

    fun anyPeriodPressed(start_time: Int, period: Int, vararg keys: Key) =
        from(keys).any { key -> periodPressed(start_time, period, key) }

    fun allPeriodPressed(period: Int, vararg keys: Key) = from(keys).all { key -> periodPressed(period, key) }

    fun anyPeriodPressed(period: Int, vararg keys: Key) = from(keys).any { key -> periodPressed(period, key) }

    ///////////////////////////////////////////////////////////////////////////////////////
}
