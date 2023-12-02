package com.uzery.fglib.core.program

import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.PointN

internal object Program {
    private val core = object: Extension() {}

    fun init(vararg ets: Extension) {
        core.children.addAll(ets)

        core.initWithChildren()
    }
    fun loop() {
        core.updateTasksWithChildren()
        core.updateWithChildren()
        core.drawWithChildren(core.draw_pos)

        Platform.update()
        program_time++
    }

    var program_time = 0
        private set
}
