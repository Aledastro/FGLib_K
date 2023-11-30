package com.uzery.fglib.extension.room_editor

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.keyboard
import com.uzery.fglib.utils.data.getter.AbstractClassGetter
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.input.KeyCode

class RoomEditor(private val GRID: Int, private var getter: (Int) -> Pair<AbstractClassGetter<GameObject>, Array<String>>): Extension() {
    var id = 0

    override fun init() {
        children.clear()
        val instance = RoomEditorInstance(GRID, getter(id))
        add(instance)
        instance.show()
    }

    private fun start() {
        children.clear()
        val instance = RoomEditorInstance(GRID, getter(id))
        add(instance)
        instance.show()
        instance.init()
    }

    override fun update() {
        if (keyboard.inPressed(KeyCode.PAGE_UP)) {
            id--
            start()
        }
        if (keyboard.inPressed(KeyCode.PAGE_DOWN)) {
            id++
            start()
        }
    }
}
