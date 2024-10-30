package com.uzery.fglib.core.obj

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.HavingComponentSyntax
import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.ObjectTransform
import com.uzery.fglib.core.component.ability.AbilityBox
import com.uzery.fglib.core.component.bounds.BoundsBox
import com.uzery.fglib.core.component.bounds.BoundsComponent
import com.uzery.fglib.core.component.controller.Controller
import com.uzery.fglib.core.component.listener.ActionListener
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.component.property.GameProperty
import com.uzery.fglib.core.component.reaction.*
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN

abstract class GameObject: HavingComponentSyntax {
    val stats = Stats()

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    val bounds = BoundsBox()

    private val controllers = ArrayList<Controller>()

    val visuals = ArrayList<Visualiser>()
    private val abilities = ArrayList<AbilityBox>()
    private val listeners = ArrayList<ActionListener>()
    private val properties = ArrayList<GameProperty>()

    private val onInit = ArrayList<OnInitComponent>()
    private val onLoad = ArrayList<OnLoadComponent>()
    private val onBirth = ArrayList<OnBirthComponent>()
    private val onDeath = ArrayList<OnDeathComponent>()
    private val onGrab = ArrayList<OnGrabComponent>()

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    internal val children = ArrayList<GameObject>()
    internal val followers = ArrayList<GameObject>()
    var owner: GameObject? = null

    val main_owner: GameObject
        get() = owner?.main_owner ?: this

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
    private val effects = HashSet<TagEffect>()

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

    override fun addComponent(vararg component: ObjectComponent) {
        for (c in component) {
            when (c) {
                is GroupComponent -> c.components.forEach { addComponent(it) }

                is BoundsComponent -> bounds[c.code.ordinal].add(c.element)

                is AbilityBox -> abilities.add(c)
                is Controller -> controllers.add(c)
                is ActionListener -> listeners.add(c)
                is GameProperty -> properties.add(c)
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

    private var inited = false
    fun init() {
        if (inited) return

        onInit.forEach { it.run() }
        onLoad.forEach { it.run() }
        inited = true
    }

    fun next() {
        if (object_time == 0) onBirth.forEach { it.run() }

        controllers.forEach { it.update() }

        abilities.forEach { it.run() }

        properties.forEach { it.update() }

        effects.forEach { it.update() }
        effects.removeIf { it.dead }

        object_time++
    }

    fun nextWithFollowers() {
        next()
        followers.removeIf { it.dead }

        followers.forEach { it.nextWithFollowers() }
    }

    fun draw(draw_pos: PointN) {
        visuals.sortBy { v -> v.drawLayer().sort }
        visuals.forEach { it.drawWithDefaults(draw_pos) }
    }

    protected fun produce(vararg os: GameObject) {
        children.addAll(os)
    }

    protected fun produce(os: List<GameObject>) {
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

    fun activate(action: InputAction) {
        listeners.forEach { it.activate(action) }
        controllers.forEach { it.activate(action) }
    }

    open fun interact() = false

    open fun collapse() {
        if (dead) return
        onDeath.forEach { it.run() }
        dead = true
        followers.forEach { it.collapse() }
    }

    fun tag(vararg tag: String) = tags.addAll(tag)
    fun untag(vararg tag: String) = tags.removeAll(tag.toSet())

    fun tagged(tag: String) = tag in tags
    fun addEffect(vararg effect: TagEffect) = effects.addAll(effect)
    fun effected(effect: String) = effects.any { it.name == effect }
    fun effectedAny(vararg effect: String) = effect.any { effected(it) }
    fun effectedAll(vararg effect: String) = effect.all { effected(it) }

    fun equalsS(other: GameObject): Boolean {
        return this.toString() == other.toString()
    }

    fun equalsName(other: GameObject): Boolean {
        return this.name == other.name
    }

    open fun answer(question: String) = false
    open fun answerS(question: String) = ""

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
}
