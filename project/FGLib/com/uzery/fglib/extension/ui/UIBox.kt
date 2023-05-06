package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.paint.Color
import java.util.*

class UIBox: Extension {
    private var active: UIElement? = null

    companion object {
        private val list = LinkedList<UIElement>()
        fun add(vararg element: UIElement) = list.addAll(element)
    }

    override fun init() {

    }

    override fun update() {
        active = list.stream().filter { a -> RectN(a.pos, a.size).into(mouse.pos()) }
            .sorted { o1, o2 -> -o1.priority.compareTo(o2.priority) }.findFirst().get()
        active?.ifActive()
        list.forEach { o -> o.update() }

        list.forEach { o ->
            Platform.graphics.fill.rect(
                o.pos,
                o.size,
                FGUtils.transparent(Color.DARKBLUE,0.1))
        }
        list.forEach { o -> o.draw() }
    }
}
