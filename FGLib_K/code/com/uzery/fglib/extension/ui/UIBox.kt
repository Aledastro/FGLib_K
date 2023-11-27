package com.uzery.fglib.extension.ui

import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform.develop_mode
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import java.util.*

@Deprecated("Use <Extension> instead")
open class UIBox(vararg elements: UIElement): Extension() {
    private var active_el: UIElement? = null

    private val list = ArrayList<UIElement>()
    private val old_list = ArrayList<UIElement>()
    private val new_list = ArrayList<UIElement>()

    init {
        list.addAll(elements)
    }

    final override fun update() {
        list.addAll(new_list)
        new_list.clear()

        list.forEach { it.active = false }

        active_el = list.filter { it.showing && it.isAt() }.maxByOrNull { it.priority }

        active_el?.ifActive()
        active_el?.active = true

        list.forEach { it.update() }

        list.removeAll(old_list)
        old_list.clear()
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

    fun remove(element: UIElement) {
        old_list.add(element)
    }

    fun add(element: UIElement) {
        new_list.add(element)
    }

    fun add(vararg elements: UIElement) = new_list.addAll(elements)
}
