package com.uzery.fglib.core.program

import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.core.program.Platform.options
import com.uzery.fglib.core.program.extension.Extension
import com.uzery.fglib.utils.audio.AudioData
import com.uzery.fglib.utils.data.file.TextData
import com.uzery.fglib.utils.data.image.ImageData
import com.uzery.fglib.utils.graphics.render.GraphicsRender
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
object Program {
    internal val core = object: Extension() {
        override fun update() {
            stats.pos = PointN(0, 0)
            stats.size = Platform.CANVAS
        }

        override fun draw(render: GraphicsRender) {
            if (FGLibSettings.MANUAL_CLEAR_ON) return

            graphics.fill.rect(render.pos, render.size, FGLibSettings.CLEAR_COLOR)
        }
    }

    fun setCanvas() {
        Platform.CANVAS = options.resize_method.adapt(options.canvas_size)
    }

    fun init(vararg ets: Extension) {
        setCanvas()

        core.add(*ets)
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
        setCanvas()

        updatePaths()

        core.rearrangeWithChildren()
        core.updateStatsWithChildren()
        Platform.extension_at_top = core.getAtTop(mouse.pos)
        core.updateTasksWithChildren()
        core.updateWithChildren()
        core.updateStatsWithChildren()

        Platform.update()
        program_time++
    }

    fun draw() {
        core.drawWithChildren(GraphicsRender(core.stats.real_pos, core.stats.real_size))
    }

    var program_time = 0
        private set
}
