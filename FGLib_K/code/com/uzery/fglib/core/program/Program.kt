package com.uzery.fglib.core.program


object Program {
    private val core = object: Extension() {}

    fun init(vararg ets: Extension) {
        core.add(*ets)

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
