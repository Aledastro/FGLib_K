package com.uzery.fglib.utils.math.scale

data class AnimationScale(val startTime: Long, val frames: Double, val scale: GradientScale) {
    constructor(startTime: Long, frames: Double, f: (Double) -> Double): this(startTime, frames, GradientScale(f))

    constructor(startTime: Int, frames: Double, scale: GradientScale): this(startTime.toLong(), frames, scale)
    constructor(startTime: Int, frames: Double, f: (Double) -> Double): this(
        startTime.toLong(),
        frames,
        GradientScale(f)
    )

    fun linear(time: Double): Double = scale.linear(get(time))
    fun linear(time: Int): Double = scale.linear(get(time.toDouble()))
    fun linear(time: Long): Double = scale.linear(get(time.toDouble()))

    fun cycled(time: Double): Double = scale.cycled(get(time))
    fun cycled(time: Int): Double = scale.cycled(get(time.toDouble()))
    fun cycled(time: Long): Double = scale.cycled(get(time.toDouble()))

    fun swing(time: Double): Double = scale.swing(get(time))
    fun swing(time: Int): Double = scale.swing(get(time.toDouble()))
    fun swing(time: Long): Double = scale.swing(get(time.toDouble()))


    private fun get(time: Double): Double = (time-startTime)/frames
}