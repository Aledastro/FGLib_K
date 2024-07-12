package com.uzery.fglib.core.program

import com.uzery.fglib.utils.audio.AudioData
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.image.ImageData


object Program {
    private val core = object: Extension() {}

    fun init(vararg ets: Extension) {
        core.add(*ets)

        core.initWithChildren()
    }

    private fun updatePaths() {
        ImageData.updatePaths()
        TextData.updatePaths()
        AudioData.updatePaths()
    }

    init {
        updatePaths()
    }

    fun update() {
        updatePaths()

        core.updateTasksWithChildren()
        core.updateWithChildren()

        Platform.update()
        program_time++
    }

    fun draw() {
        core.drawWithChildren(core.stats.render_pos)
    }

    fun loop() {
        update()
        draw()
    }

    var program_time = 0
        private set
}
