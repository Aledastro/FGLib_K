package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform.develop_mode
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.math.FGUtils
import javafx.scene.paint.Color
import java.util.*

object UIBox: Extension {
    private var active: UIElement? = null

    private val list = LinkedList<UIElement>()
    fun add(vararg element: UIElement) = list.addAll(element)

    override fun init() {

    }

    override fun update() {
        active = list.stream().filter { el -> el.showing && el.isActive() }
            .sorted { o1, o2 -> -o1.priority.compareTo(o2.priority) }.findFirst().orElse(null)
        active?.ifActive()
        list.forEach { it.update() }

        if (develop_mode) {
            list.forEach {
                if (it.showing) graphics.fill.rect(it.pos, it.size, FGUtils.transparent(Color.DARKBLUE, 0.1))
            }
            if (active != null) {
                graphics.setStroke(1.5)
                graphics.stroke.rect(active!!.pos, active!!.size, FGUtils.transparent(Color.WHITE, 0.9))
            }
        }

        list.forEach { if (it.showing) it.draw() }
    }

    fun clear() {
        list.clear()
    }
}
