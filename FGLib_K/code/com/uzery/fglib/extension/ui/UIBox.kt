package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform.develop_mode
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import java.util.*

open class UIBox(vararg elements: UIElement): Extension() {
    private var active_el: UIElement? = null

    private val list = LinkedList<UIElement>()

    init {
        list.addAll(elements)
    }

    fun add(vararg elements: UIElement) = list.addAll(elements)

    final override fun update() {
        active_el = list.stream().filter { el -> el.showing && el.isActive() }
            .sorted { o1, o2 -> -o1.priority.compareTo(o2.priority) }.findFirst().orElse(null)
        active_el?.ifActive()
        list.forEach { it.update() }
    }

    final override fun draw(pos: PointN) {
        if (develop_mode) {
            list.forEach {
                if (it.showing) graphics.fill.rect(it.pos, it.size, FGUtils.transparent(Color.DARKBLUE, 0.1))
            }
            if (active_el != null) {
                graphics.setStroke(1.5)
                graphics.stroke.rect(active_el!!.pos, active_el!!.size, FGUtils.transparent(Color.WHITE, 0.9))
            }
        }

        list.forEach { if (it.showing) it.draw() }
    }

    fun clear() {
        list.clear()
    }
}
