package com.uzery.fglib.core.program.extension

import com.uzery.fglib.core.component.bounds.Bounds
import com.uzery.fglib.core.program.DebugTools.countTime
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.getGlobalAtTop
import com.uzery.fglib.core.program.Platform.mouse
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.graphics.render.GraphicsRender
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * TODO("doc")
 **/
abstract class Extension(vararg children: Extension) {
    val children = ArrayList<Extension>()

    private val real_children = ArrayList<Extension>()
    private val arranged_children = ArrayList<Extension>()
    private val new_children = ArrayList<ExtensionEntry>()

    val stats = ExtensionStats()

    val DEFAULT_BOUNDS = Bounds { if (stats.size != PointN.ZERO) RectN(stats.size) else null }

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

    val graphics
        get() = Platform.graphics

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    enum class MODE(val draw: Boolean, val update: Boolean) {
        SHOW(true, true),
        HIDE(false, false),
        ONLY_DRAW(true, false),
        ONLY_UPDATE(false, true);

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

    open fun draw(render: GraphicsRender) {}
    open fun drawAfter(render: GraphicsRender) {}

    open fun onShow() {}
    open fun onHide() {}

    open fun onOnlyDraw() {}
    open fun onOnlyUpdate() {}

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var inited = false
    internal fun initWithChildren() {
        if (inited) return

        modify(false)
        init()
        modify(false)
        load()
        rearrange()
        arranged_children.forEach { it.initWithChildren() }
        initAfter()

        inited = true
    }

    private fun modify(with_init: Boolean) {
        val toAdd = ArrayList<Extension>()
        val toRemove = ArrayList<Extension>()

        new_children.forEach { entry ->
            toAdd.remove(entry.e)
            toRemove.remove(entry.e)

            if (entry.isAdd) toAdd.add(entry.e)
            else toRemove.add(entry.e)

            entry.e.stats.parent = this
        }
        if (with_init) new_children.forEach { it.e.initWithChildren() }
        new_children.clear()

        real_children.removeAll(toRemove.toSet())
        real_children.removeAll(toAdd.toSet())
        real_children.addAll(toAdd)

        real_children.removeIf { it.ready_to_collapse }

        children.clear()
        children.addAll(real_children)
    }

    private val java_code = this.javaClass.name

    internal fun updateWithChildren() {
        stats.next()

        countTime("update", java_code) {
            update()
        }

        arranged_children.filter { it.is_updating }.forEach { e ->
            e.updateWithChildren()
        }
        countTime("update", java_code) {
            updateAfter()
        }

        update_time++
    }

    internal fun drawWithChildren(render: GraphicsRender) {
        fun reset() {
            graphics.setFullDefaults()
            graphics.global_transform = stats.full_transform*graphics.default_transform
        }

        countTime("draw", java_code) {
            reset()
            draw(render)
        }

        arranged_children.filter { it.is_drawing }.forEach { e ->
            e.drawWithChildren(render.child(e.stats.render_pos, e.stats.size))
        }

        countTime("draw", java_code) {
            reset()
            drawAfter(render)
        }

        draw_time++
    }

    internal fun updateTasksWithChildren() {
        mode = next_mode

        countTime("onBackGround", java_code) {
            onBackGround()
        }

        arranged_children.forEach { e -> e.updateTasksWithChildren() }

        full_time++
    }

    private fun rearrange() {
        modify(true)

        arranged_children.clear()
        arranged_children.addAll(real_children)

        arranged_children.sortBy { it.stats.ui_level.level }
    }

    internal fun rearrangeWithChildren() {
        rearrange()

        arranged_children.forEach { e -> e.stats.parent = this }
        arranged_children.forEach { e -> e.rearrangeWithChildren() }
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

    fun onlyDraw() {
        next_mode = MODE.ONLY_DRAW
        onOnlyDraw()
    }

    fun onlyUpdate() {
        next_mode = MODE.ONLY_UPDATE
        onOnlyUpdate()
    }

    fun switchTo(show: Boolean) {
        if (show) show()
        else hide()
    }

    fun switchTo(mode: MODE) {
        when (mode) {
            MODE.SHOW -> show()
            MODE.HIDE -> hide()
            MODE.ONLY_DRAW -> onlyDraw()
            MODE.ONLY_UPDATE -> onlyUpdate()
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

    fun getParent(depth: Int = 1): Extension {
        if (depth < 1) throw DebugData.error("depth should be positive")

        var current: Extension = this
        var remained = depth
        while (true) {
            current = current.stats.parent ?: throw DebugData.error("No parent found")
            remained--

            if (remained == 0) return current
        }
    }

    @JvmName("getTypedParent")
    inline fun <reified T: Extension> getParent(depth: Int = 1): T {
        if (depth < 1) throw DebugData.error("depth should be positive")

        var current: Extension = this
        var remained = depth
        while (true) {
            current = current.stats.parent ?: throw DebugData.error("No parent found")

            if (current is T) {
                remained--
                if (remained == 0) return current
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun posIn(bounds: Bounds?, pos: PointN): Boolean {
        return bounds?.into(pos-stats.real_pos) ?: false
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun getAtTop(pos: PointN): Extension? {
        if (posIn(stats.bounds_after, pos)) return this

        var res: Extension? = null

        if (posIn(stats.bounds, pos)) res = this

        for (child in arranged_children) {
            if (!child.is_drawing) continue
            val child_res = child.getAtTop(pos) ?: continue
            res = child_res
        }

        return res
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun posInBounds(pos: PointN) = posIn(stats.bounds, pos)
    fun posInBoundsAfter(pos: PointN) = posIn(stats.bounds_after, pos)

    fun posAtLocalTop(pos: PointN): Boolean {
        return getAtTop(pos) == this
    }

    fun posAtTop(pos: PointN): Boolean {
        return getGlobalAtTop(pos) == this
    }

    fun posAtTopWithChildren(pos: PointN): Boolean {
        val global = getGlobalAtTop(pos)
        return global != null && global == getAtTop(pos)
    }

    fun posInBounds(find: PointN, area: RectN) = posInBounds(find) && area.into(find-stats.real_pos)
    fun posInBounds(find: PointN, pos: PointN, size: PointN) = posInBounds(find, RectN(pos, size))

    fun posInBoundsAfter(find: PointN, area: RectN) = posInBoundsAfter(find) && area.into(find-stats.real_pos)
    fun posInBoundsAfter(find: PointN, pos: PointN, size: PointN) = posInBoundsAfter(find, RectN(pos, size))

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun mousePos() = mouse.pos-stats.real_pos

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun mouseInBounds() = posIn(stats.bounds, mouse.pos)
    fun mouseInBoundsAfter() = posIn(stats.bounds_after, mouse.pos)

    fun mouseAtLocalTop() = posAtLocalTop(mouse.pos)

    fun mouseAtTop(): Boolean {
        return Platform.extension_at_top == this
    }

    fun mouseAtTopWithChildren(): Boolean {
        val global = Platform.extension_at_top
        return global != null && global == getAtTop(mouse.pos)
    }

    fun mouseInBounds(area: RectN) = mouseInBounds() && area.into(mousePos())
    fun mouseInBounds(pos: PointN, size: PointN) = mouseInBounds(RectN(pos, size))

    fun mouseInBoundsAfter(area: RectN) = mouseInBoundsAfter() && area.into(mousePos())
    fun mouseInBoundsAfter(pos: PointN, size: PointN) = mouseInBoundsAfter(RectN(pos, size))

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
