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

    fun loop() {
        updatePaths()

        core.updateTasksWithChildren()
        core.updateWithChildren()
        core.drawWithChildren(core.data.render_pos)

        Platform.update()
        program_time++
    }

    var program_time = 0
        private set
}
