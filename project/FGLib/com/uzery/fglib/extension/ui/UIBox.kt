package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.utils.math.FGUtils
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
        active = list.stream().filter { el -> el.isA(el.pos, el.size) }
            .sorted { o1, o2 -> -o1.priority.compareTo(o2.priority) }.findFirst().orElse(null)
        active?.ifActive()
        list.forEach { o -> o.update() }

        list.forEach { o ->
            graphics.fill.rect(
                o.pos,
                o.size,
                FGUtils.transparent(Color.DARKBLUE, 0.1))
        }
        if(active != null) {
            graphics.setStroke(1.5)
            graphics.stroke.rect(active!!.pos, active!!.size, FGUtils.transparent(Color.WHITE, 0.9))
        }


        list.forEach { o -> o.draw() }
    }
}
