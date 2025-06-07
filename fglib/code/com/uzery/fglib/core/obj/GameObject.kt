package com.uzery.fglib.core.obj

import com.uzery.fglib.core.component.ComponentFunctionality
import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.ability.AbilityBox
import com.uzery.fglib.core.component.bounds.BoundsBox
import com.uzery.fglib.core.component.bounds.BoundsComponent
import com.uzery.fglib.core.component.controller.Controller
import com.uzery.fglib.core.component.group.GroupComponent
import com.uzery.fglib.core.component.listener.ActionListener
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.component.reaction.*
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.data.entry.FGFormat
import com.uzery.fglib.utils.data.getter.ClassGetter
import com.uzery.fglib.utils.data.getter.value.PosValue
import com.uzery.fglib.utils.data.getter.value.SizeValue
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

/**
 * Base class for entity in *Entity-Component System*
 *
 * Is final form of game logic
 *
 * Contains stats, components, transforms and export info
 *
 * Use serialisation via [FGFormat].
 * To make it serializable, you need to add export info (`exportInfo()`) and add entry in [ClassGetter]
 *
 * @see [Stats]
 * @see [ObjectComponent]
 * @see [ObjectTransform]
 * @see [ComponentFunctionality]
 **/
abstract class GameObject: ComponentFunctionality() {
    val stats = Stats()

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val bounds_elements = ArrayList<BoundsComponent>()
    lateinit var bounds: BoundsBox

    private val abilities = ArrayList<AbilityBox>()
    private val controllers = ArrayList<Controller>()
    private val listeners = ArrayList<ActionListener>()
    val visuals = ArrayList<Visualiser>()

    private val onInit = ArrayList<OnInitComponent>()
    private val onLoad = ArrayList<OnLoadComponent>()
    private val onBirth = ArrayList<OnBirthComponent>()
    private val onDeath = ArrayList<OnDeathComponent>()
    private val onGrab = ArrayList<OnGrabComponent>()

    final override fun addComponent(vararg component: ObjectComponent) {
        for (c in component) {
            when (c) {
                is GroupComponent -> c.components.forEach { addComponent(it) }

                is BoundsComponent -> bounds_elements.add(c)

                is AbilityBox -> abilities.add(c)
                is Controller -> controllers.add(c)
                is ActionListener -> listeners.add(c)
                is Visualiser -> visuals.add(c)

                is OnInitComponent -> onInit.add(c)
                is OnLoadComponent -> onLoad.add(c)
                is OnBirthComponent -> onBirth.add(c)
                is OnDeathComponent -> onDeath.add(c)
                is OnGrabComponent -> onGrab.add(c)

                else -> throw DebugData.error("Wrong Component: $c")
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    internal val children = ArrayList<GameObject>()
    val followers = ArrayList<GameObject>()
    var owner: GameObject? = null

    val top_owner: GameObject
        get() = owner?.top_owner ?: this

    val pos_with_owners: PointN
        get() {
            var pos = stats.POS
            var current = owner
            while (current != null) {
                pos += current.stats.POS
                current = current.owner
            }
            return pos
        }

    private val tags = HashSet<String>()

    var dead = false
        private set

    var object_time = 0
        private set

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    val transform: ObjectTransform?
        get() {
            if (transforms.isEmpty()) return null
            return transforms[transformID.coerceIn(transforms.indices)]
        }
    val transforms = ArrayList<ObjectTransform>()
    var transformID = -1

    fun addTransform(vararg transform: ObjectTransform) {
        transforms.addAll(transform.toList())
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var inited = false
    fun init() {
        if (inited) return

        onInit.forEach { it.run() }
        onLoad.forEach { it.run() }

        bounds = BoundsBox(bounds_elements)

        inited = true
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun next() {
        nextLogics()
        nextTime()
    }

    private fun nextLogics() {
        bounds = BoundsBox(bounds_elements)

        if (object_time == 0) onBirth.forEach { it.run() }

        controllers.forEach { it.update() }
        abilities.forEach { it.run() }

        setCoverArea()
    }

    var cover_area: RectN? = null
        private set

    private fun setCoverArea() {
        val areas = ArrayList<RectN>()
        for (code in bounds.codes) {
            bounds[code].cover_area?.let { area -> areas.add(area) }
        }
        cover_area = if (areas.isEmpty()) null
        else ShapeUtils.coverAreaOf(*areas.toTypedArray())
    }

    private fun nextTime() {
        object_time++
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    internal fun nextWithFollowers() {
        next()
        followers.removeIf { it.dead }

        followers.forEach { it.nextWithFollowers() }
    }

    internal fun nextLogicsWithFollowers() {
        nextLogics()
        followers.removeIf { it.dead }

        followers.forEach { it.nextLogicsWithFollowers() }
    }

    internal fun nextTimeWithFollowers() {
        nextTime()

        followers.forEach { it.nextTimeWithFollowers() }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(draw_pos: PointN) {
        visuals.sort()
        visuals.forEach { it.drawWithDefaults(draw_pos) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun produce(vararg os: GameObject) {
        children.addAll(os)
    }

    fun grab(vararg os: GameObject) {
        followers.addAll(os)
        followers.forEach { it.init() }
        os.forEach { it.owner = this }
        os.forEach { o -> o.onGrab.forEach { it.run() } }
    }

    fun grab(os: List<GameObject>) {
        followers.addAll(os)
        followers.forEach { it.init() }
        os.forEach { it.owner = this }
        os.forEach { o -> o.onGrab.forEach { it.run() } }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun activate(action: InputAction) {
        listeners.forEach { it.activate(action) }
        controllers.forEach { it.activate(action) }
    }

    open fun interact() = false

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    open fun collapse() {
        if (dead) return
        onDeath.forEach { it.run() }
        dead = true
        followers.forEach { it.collapse() }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun tag(vararg tag: String) = tags.addAll(tag)
    fun untag(vararg tag: String) = tags.removeAll(tag.toSet())

    fun tagged(tag: String) = tag in tags

    open fun answer(question: String) = false
    open fun answerS(question: String) = ""

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun equalsS(other: GameObject): Boolean {
        return this.toString() == other.toString()
    }

    fun equalsName(other: GameObject): Boolean {
        return this.name == other.name
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private val TEMP_NAME = "temp"
    var name: String = TEMP_NAME
        private set
    private var values: (() -> List<Any>)? = null

    fun isTemp() = name == TEMP_NAME

    fun exportInfo(name: String, values: (() -> List<Any>)?) {
        this.name = name
        this.values = values
    }

    final override fun toString(): String {
        val values = values?.invoke() ?: emptyList()

        return buildString {
            append(name)

            if (values.isNotEmpty()) {
                append(":")
                values.forEach { value ->
                    val s = value.toString()
                    if (s == "") throw DebugData.error("NULLABLE VALUE: $name: $values")
                    append(if (s[s.lastIndex] == ']') " $s" else " [$s]")
                }
            }
        }
    }

    fun toEntry(): FGEntry {
        return FGFormat.entryFrom(toString())
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected val ei_pos = {
        listOf(
            PosValue(stats.POS)
        )
    }
    protected val ei_pos_size = {
        listOf(
            PosValue(stats.POS),
            SizeValue(stats.SIZE)
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    final override fun hashCode(): Int {
        return super.hashCode()
    }

    final override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
