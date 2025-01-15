package com.uzery.fglib.core.program.extension

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.getGlobalOnTop
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.math.geom.PointN

/**
 * TODO("doc")
 **/
abstract class Extension(vararg children: Extension) {
    val children = ArrayList<Extension>()

    private val real_children = ArrayList<Extension>()
    private val arranged_children = ArrayList<Extension>()
    private val new_children = ArrayList<ExtensionEntry>()

    val stats = ExtensionData()

    protected var full_time = 0
        private set
    protected var update_time = 0
        private set
    protected var draw_time = 0
        private set

    val is_drawing
        get() = mode.draw && active().draw
    val is_updating
        get() = mode.update && active().update

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    enum class MODE(val draw: Boolean, val update: Boolean) {
        SHOW(true, true),
        HIDE(false, false),
        ONLY_DRAW(true, false),
        ONLY_UPDATE(false, true);

        fun active() = draw || update

        operator fun unaryMinus(): MODE {
            return when (this) {
                SHOW -> HIDE
                HIDE -> SHOW
                ONLY_DRAW -> ONLY_UPDATE
                ONLY_UPDATE -> ONLY_DRAW
            }
        }
    }

    var mode = MODE.SHOW
        private set
    private var next_mode = MODE.SHOW

    open fun active() = MODE.SHOW

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun add(vararg children: Extension) {
        children.forEach { e -> new_children.add(ExtensionEntry.ADD(e)) }
    }

    fun addOn(level: UILevel, vararg children: Extension) {
        children.forEach { e ->
            e.stats.ui_level = level
            new_children.add(ExtensionEntry.ADD(e))
        }
    }

    fun remove(vararg children: Extension) {
        children.forEach { e -> new_children.add(ExtensionEntry.REMOVE(e)) }
    }

    fun clearChildren() = remove(*arranged_children.toTypedArray())

    init {
        add(*children)
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected open fun init() {}
    protected open fun initAfter() {}

    open fun update() {}
    open fun updateAfter() {}

    open fun draw(pos: PointN) {}
    open fun drawAfter(pos: PointN) {}

    open fun onShow() {}
    open fun onHide() {}

    open fun onOnlyDraw() {}
    open fun onOnlyUpdate() {}

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var inited = false
    internal fun initWithChildren() {
        if (inited) return

        modify()
        init()
        modify()
        load()
        rearrange()
        arranged_children.forEach { it.initWithChildren() }
        initAfter()

        inited = true
    }

    private fun modify() {
        val toAdd = ArrayList<Extension>()
        val toRemove = ArrayList<Extension>()

        new_children.forEach { entry ->
            toAdd.remove(entry.e)
            toRemove.remove(entry.e)

            if (entry.isAdd) toAdd.add(entry.e)
            else toRemove.add(entry.e)
        }
        new_children.forEach { it.e.initWithChildren() }
        new_children.clear()

        real_children.removeAll(toRemove.toSet())
        real_children.removeAll(toAdd.toSet())
        real_children.addAll(toAdd.toSet())

        real_children.removeIf { it.ready_to_collapse }

        children.clear()
        children.addAll(real_children)
    }

    internal fun updateAllWithChildren() {
        rearrangeWithChildren()
        Platform.extension_at_top = getOnTop(mouse.pos)
        updateTasksWithChildren()
        updateWithChildren()
    }

    private fun updateWithChildren() {
        stats.next()

        update()
        arranged_children.filter { it.is_updating }.forEach { e ->
            e.updateWithChildren()
        }
        updateAfter()

        update_time++
    }

    internal fun drawWithChildren(pos: PointN) {
        fun reset() {
            graphics.setFullDefaults()
            graphics.global_transform = stats.full_transform*graphics.default_transform
        }
        reset()
        draw(pos)

        arranged_children.filter { it.is_drawing }.forEach { e ->
            e.drawWithChildren(pos+e.stats.render_pos)
        }

        reset()
        drawAfter(pos)

        draw_time++
    }

    private fun updateTasksWithChildren() {
        mode = next_mode
        onBackGround()

        arranged_children.forEach { e ->
            e.stats.owner = this
            e.updateTasksWithChildren()
        }

        full_time++
    }

    private fun rearrange() {
        modify()

        arranged_children.clear()
        arranged_children.addAll(real_children)

        arranged_children.sortBy { it.stats.ui_level.level }
    }

    private fun rearrangeWithChildren() {
        rearrange()

        arranged_children.forEach { it.rearrangeWithChildren() }
    }

    open fun onBackGround() {}

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun show() {
        next_mode = MODE.SHOW
        onShow()
    }

    fun hide() {
        next_mode = MODE.HIDE
        onHide()
    }

    fun only_draw() {
        next_mode = MODE.ONLY_DRAW
        onOnlyDraw()
    }

    fun only_update() {
        next_mode = MODE.ONLY_UPDATE
        onOnlyDraw()
    }

    fun switchTo(show: Boolean) {
        if (show) show()
        else hide()
    }

    fun switchTo(mode: MODE) {
        when (mode) {
            MODE.SHOW -> show()
            MODE.HIDE -> hide()
            MODE.ONLY_DRAW -> only_draw()
            MODE.ONLY_UPDATE -> only_update()
        }
    }

    fun switch() = switchTo(-mode)

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    open fun load() {}

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var ready_to_collapse = false
    fun collapse() {
        ready_to_collapse = true
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getOnTop(pos: PointN): Extension? {
        var res: Extension? = null
        if (posIn(pos)) res = this

        for (e in arranged_children) {
            if (!e.is_drawing) continue
            val e_res = e.getOnTop(pos) ?: continue
            res = e_res
        }

        return res
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun posIn(pos: PointN): Boolean {
        return stats.bounds?.into(pos-stats.real_pos) ?: false
    }
    fun posAt(pos: PointN): Boolean {
        fun notAtChildren(now: Extension): Boolean {
            return now.children.all { e -> !e.posIn(pos) && notAtChildren(e) }
        }
        return posIn(pos) && notAtChildren(this)
    }
    fun posAtTop(pos: PointN): Boolean {
        return getGlobalOnTop(pos) == this
    }
    fun posAtTopWithChildren(pos: PointN): Boolean {
        return getGlobalOnTop(pos) == getOnTop(mouse.pos)
    }

    fun mousePos() = mouse.pos-stats.real_pos

    fun mouseIn() = posIn(mouse.pos)
    fun mouseAt() = posAt(mouse.pos)
    fun mouseAtTop(): Boolean {
        return Platform.extension_at_top == this
    }
    fun mouseAtTopWithChildren(): Boolean {
        return Platform.extension_at_top == getOnTop(mouse.pos)
    }
}
