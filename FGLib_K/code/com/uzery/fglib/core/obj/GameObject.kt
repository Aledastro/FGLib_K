package com.uzery.fglib.core.obj

import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.ActionListener
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.obj.bounds.BoundsBox.Companion.CODE
import com.uzery.fglib.core.obj.bounds.BoundsComponent
import com.uzery.fglib.core.obj.bounds.BoundsElement
import com.uzery.fglib.core.obj.component.*
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.property.GameProperty
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape

abstract class GameObject(var name: String = "temp") {
    val stats = Stats()
    val bounds = BoundsBox()

    private var controller: Controller? = null
    private var temp: TempAction? = null

    val visuals = ArrayList<Visualiser>()
    private val abilities = ArrayList<AbilityBox>()
    private val listeners = ArrayList<ActionListener>()
    private val properties = ArrayList<GameProperty>()

    private val onInit = ArrayList<OnInitComponent>()
    private val onLoad = ArrayList<OnLoadComponent>()
    private val onBirth = ArrayList<OnBirthComponent>()
    private val onDeath = ArrayList<OnDeathComponent>()
    private val onGrab = ArrayList<OnGrabComponent>()

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

    val values = ArrayList<Any>()

    var dead = false
        private set

    var object_time = 0
        private set

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

    fun addComponent(vararg component: ObjectComponent) {
        for (c in component) {
            when (c) {
                is GroupComponent -> c.components.forEach { addComponent(it) }

                is BoundsComponent -> addBounds(c.code, c.element)

                is AbilityBox -> addAbility(c)
                is Controller -> setController(c)
                is ActionListener -> addListener(c)
                is GameProperty -> addProperty(c)
                is Visualiser -> addVisual(c)

                is OnInitComponent -> onInit(c)
                is OnLoadComponent -> onLoad(c)
                is OnBirthComponent -> onBirth(c)
                is OnDeathComponent -> onDeath(c)
                is OnGrabComponent -> onGrab(c)

                else -> throw DebugData.error("Wrong Component: $c")
            }
        }
    }

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


    fun setController(controller: () -> (() -> TempAction)) {
        setController(Controller { controller() })
    }

    fun setController(controller: Controller) {
        this.controller = controller
    }

    fun addListener(listener: (InputAction) -> Unit) = addListener(ActionListener { listener(it) })
    fun addListener(vararg listener: ActionListener) = listeners.addAll(listener)
    fun addAbility(ability: () -> Unit) = addAbility(AbilityBox { ability() })
    fun addAbility(vararg ability: AbilityBox) = abilities.addAll(ability)
    fun addProperty(vararg property: GameProperty) = properties.addAll(property)
    fun addVisual(vararg vis: Visualiser) = visuals.addAll(vis)

    fun addVisual(layer: DrawLayer, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        visuals.add(
            object: LayerVisualiser(layer) {
                override fun draw(draw_pos: PointN) {
                    vis(agc, draw_pos)
                }
            }
        )
    }

    fun addVisual(layer: DrawLayer, sort_pos: PointN, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        visuals.add(
            object: LayerVisualiser(layer) {
                override fun draw(draw_pos: PointN) {
                    vis(agc, draw_pos)
                }

                override val sortPOS: PointN
                    get() = sort_pos
            }
        )
    }

    fun onInit(f: () -> Unit) = onInit.add(f)
    fun onInit(f: OnInitComponent) = onInit.add(f)

    fun onLoad(f: () -> Unit) = onLoad.add(f)
    fun onLoad(f: OnLoadComponent) = onLoad.add(f)

    fun onBirth(f: () -> Unit) = onBirth.add(f)
    fun onBirth(f: OnBirthComponent) = onBirth.add(f)

    fun onDeath(f: () -> Unit) = onDeath.add(f)
    fun onDeath(f: OnDeathComponent) = onDeath.add(f)

    fun onGrab(f: () -> Unit) = onGrab.add(f)
    fun onGrab(f: OnGrabComponent) = onGrab.add(f)

    private var inited = false
    fun init() {
        if (inited) return

        onInit.forEach { it.run() }
        onLoad.forEach { it.run() }
        inited = true
    }

    fun next() {
        if (object_time == 0) onBirth.forEach { it.run() }

        if (temp == null || temp!!.ends) temp = controller?.get()?.invoke()
        temp?.next()

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
        //todo followers.forEach { it.init() }
        os.forEach { it.owner = this }
        os.forEach { o -> o.onGrab.forEach { it.run() } }
    }

    fun grab(os: List<GameObject>) {
        followers.addAll(os)
        //todo followers.forEach { it.init() }
        os.forEach { it.owner = this }
        os.forEach { o -> o.onGrab.forEach { it.run() } }
    }

    fun activate(action: InputAction) {
        listeners.forEach { a -> a.activate(action) }
        temp?.activate(action)
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

    protected open fun setValues() {}

    final override fun toString(): String {
        values.clear()
        setValues()
        val res = StringBuilder(name)
        if (values.isNotEmpty()) {
            res.append(":")
            values.forEach { value ->
                val s = value.toString()
                if (s == "") throw DebugData.error("NULLABLE VALUE: $name: $values")
                res.append(if (s[s.lastIndex] == ']') " $s" else " [$s]")
            }
        }
        return res.toString()
    }
}
