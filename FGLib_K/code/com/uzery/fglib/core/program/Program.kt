package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.CANVAS_R
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.extension.Extension
import com.uzery.fglib.utils.audio.AudioData
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
object Program {
    internal val core = object: Extension() {
        override fun draw(pos: PointN) {
            if (FGLibSettings.MANUAL_CLEAR_ON) return

            graphics.fill.draw(CANVAS_R, FGLibSettings.CLEAR_COLOR)
        }
    }

    fun init(vararg ets: Extension) {
        core.add(*ets)

        core.stats.pos = PointN(0, 0)
        core.stats.size = CANVAS
        core.initWithChildren()
    }

    private fun updatePaths() {
        ImageData.paths.updatePaths()
        TextData.paths.updatePaths()
        AudioData.paths.updatePaths()
    }

    init {
        updatePaths()
    }

    fun update() {
        updatePaths()

        core.updateAllWithChildren()

        Platform.update()
        program_time++
    }

    fun draw() {
        core.drawWithChildren(core.stats.render_pos)
    }

    var program_time = 0
        private set
}
