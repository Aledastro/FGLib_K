package com.uzery.fglib.core.obj

import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.ActionListener
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.obj.bounds.BoundsBox.Companion.CODE
import com.uzery.fglib.core.obj.bounds.BoundsElement
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.property.GameProperty
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import java.util.*

abstract class GameObject {
    val stats = Stats()
    var bounds = BoundsBox()

    private var controller: Controller? = null
    private var temp: TempAction? = null

    val visuals = LinkedList<Visualiser>()
    internal val abilities = LinkedList<AbilityBox>()
    private val listeners = LinkedList<ActionListener>()
    private val properties = LinkedList<GameProperty>()

    internal val children = LinkedList<GameObject>()
    internal val followers = LinkedList<GameObject>()
    var owner: GameObject? = null

    private val tags = LinkedList<String>()
    private val effects = LinkedList<TagEffect>()

    var name = "temp"
    val values = LinkedList<Any>()

    var dead = false
        private set

    var object_time = 0
        private set

    private fun addBounds(code: CODE, vararg bs: BoundsElement) = bounds[code.ordinal].add(*bs)
    private fun addBounds(code: CODE, shape: () -> Shape?) = bounds[code.ordinal].add(shape)
    private fun addBounds(code: CODE, name: String, shape: () -> Shape?) =
        bounds[code.ordinal].add(BoundsElement(name, shape))

    fun addRedBounds(vararg bs: BoundsElement) = addBounds(CODE.RED, *bs)
    fun addRedBounds(shape: () -> Shape?) = addBounds(CODE.RED, shape)
    fun addRedBounds(name: String, shape: () -> Shape?) = addBounds(CODE.RED, name, shape)

    fun addOrangeBounds(vararg bs: BoundsElement) = addBounds(CODE.ORANGE, *bs)
    fun addOrangeBounds(shape: () -> Shape?) = addBounds(CODE.ORANGE, shape)
    fun addOrangeBounds(name: String, shape: () -> Shape?) = addBounds(CODE.ORANGE, name, shape)

    fun addBlueBounds(vararg bs: BoundsElement) = addBounds(CODE.BLUE, *bs)
    fun addBlueBounds(shape: () -> Shape?) = addBounds(CODE.BLUE, shape)
    fun addBlueBounds(name: String, shape: () -> Shape?) = addBounds(CODE.BLUE, name, shape)

    fun addGreenBounds(vararg bs: BoundsElement) = addBounds(CODE.GREEN, *bs)
    fun addGreenBounds(shape: () -> Shape?) = addBounds(CODE.GREEN, shape)
    fun addGreenBounds(name: String, shape: () -> Shape?) = addBounds(CODE.GREEN, name, shape)


    fun setController(controller: () -> () -> TempAction) {
        this.controller = Controller { controller() }
    }

    fun setController(controller: Controller) {
        this.controller = controller
    }

    fun addListener(listener: (InputAction) -> Unit) = listeners.add(ActionListener { listener(it) })
    fun addListener(vararg listener: ActionListener) = listeners.addAll(listener)
    fun addAbility(ability: () -> Unit) = abilities.add(AbilityBox { ability() })
    fun addAbility(vararg ability: AbilityBox) = abilities.addAll(ability)
    fun addProperty(property: () -> Unit) = properties.add(GameProperty { property() })
    fun addProperty(vararg property: GameProperty) = properties.addAll(property)
    fun addVisual(visual: Visualiser) = visuals.add(visual)

    fun next() {
        if (object_time == 0) afterInit()

        if (temp == null || temp!!.ends) temp = controller?.get()?.invoke()
        temp?.next()

        abilities.forEach { it.run() }

        properties.forEach { it.update() }

        effects.forEach { it.update() }
        effects.removeIf { it.dead }

        object_time++
    }

    fun nextWithFollowers(){
        next()
        followers.removeIf { it.dead }

        followers.forEach { it.nextWithFollowers() }
    }

    open fun afterInit() {
        /* ignore */
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
        os.forEach { it.owner = this }
        os.forEach { it.onGrab() }
    }

    fun grab(os: List<GameObject>) {
        followers.addAll(os)
        os.forEach { o -> o.owner = this }
        os.forEach { o -> o.onGrab() }
    }

    open fun setValues() {
        name = "temp"
    }

    override fun toString(): String {
        values.clear()
        setValues()
        val s = StringBuilder(name)
        if (values.isNotEmpty()) {
            s.append(":")
            values.forEach { value ->
                val ss = value.toString()
                if (ss == "") throw DebugData.error("NULLABLE VALUE: $name: $values")
                s.append(if (ss[ss.lastIndex] == ']') " $ss" else " [$ss]")
            }
        }
        return s.toString()
    }

    fun activate(action: InputAction) {
        listeners.forEach { a -> a.activate(action) }
        temp?.activate(action)
    }

    open fun interact() = false

    open fun collapse() {
        if (dead) return
        onDeath()
        dead = true
        followers.forEach { it.collapse() }
    }

    open fun onDeath() {
        /* ignore */
    }

    open fun onGrab() {
        /* ignore */
    }

    fun tag(vararg tag: String) = tags.addAll(tag)
    fun untag(vararg tag: String) = tags.removeAll(tag.toSet())

    fun tagged(tag: String) = tags.contains(tag)
    fun addEffect(vararg effect: TagEffect) = effects.addAll(effect)
    fun effected(effect: String) = effects.any { a -> a.name == effect }
    fun effectedAny(vararg effect: String) = effect.any { eff -> effected(eff) }
    fun effectedAll(vararg effect: String) = effect.all { eff -> effected(eff) }
    fun equalsS(other: GameObject): Boolean {
        return this.toString() == other.toString()
    }

    fun equalsName(other: GameObject): Boolean {
        this.setValues()
        other.setValues()
        return this.name == other.name
    }

    open fun answerYN(question: String) = false
    open fun answer(question: String) = ""
}
