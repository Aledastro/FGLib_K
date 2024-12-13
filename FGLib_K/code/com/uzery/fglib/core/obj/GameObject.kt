package com.uzery.fglib.core.obj

import com.uzery.fglib.core.component.GroupComponent
import com.uzery.fglib.core.component.HavingComponentSyntax
import com.uzery.fglib.core.component.ObjectComponent
import com.uzery.fglib.core.component.ObjectTransform
import com.uzery.fglib.core.component.ability.AbilityBox
import com.uzery.fglib.core.component.ability.GroupAbility
import com.uzery.fglib.core.component.bounds.BoundsBox
import com.uzery.fglib.core.component.bounds.BoundsBox.Companion.CODE
import com.uzery.fglib.core.component.bounds.BoundsComponent
import com.uzery.fglib.core.component.bounds.BoundsElement
import com.uzery.fglib.core.component.bounds.GroupBounds
import com.uzery.fglib.core.component.controller.Controller
import com.uzery.fglib.core.component.controller.GroupController
import com.uzery.fglib.core.component.controller.TempAction
import com.uzery.fglib.core.component.listener.ActionListener
import com.uzery.fglib.core.component.listener.BoundsInputAction
import com.uzery.fglib.core.component.listener.GroupListener
import com.uzery.fglib.core.component.listener.InputAction
import com.uzery.fglib.core.component.load.AudioResource
import com.uzery.fglib.core.component.load.ImageResource
import com.uzery.fglib.core.component.load.SpriteResource
import com.uzery.fglib.core.component.property.GameProperty
import com.uzery.fglib.core.component.property.GroupProperty
import com.uzery.fglib.core.component.reaction.*
import com.uzery.fglib.core.component.visual.GroupVisualiser
import com.uzery.fglib.core.component.visual.LayerVisualiser
import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.graphics.AffineGraphics
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import com.uzery.fglib.utils.math.geom.shape.RectN
import com.uzery.fglib.utils.struct.num.IntI

/**
 * TODO("doc")
 **/
abstract class GameObject: HavingComponentSyntax {
    val stats = Stats()

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    val bounds = BoundsBox()

    private val abilities = ArrayList<AbilityBox>()
    private val controllers = ArrayList<Controller>()
    private val listeners = ArrayList<ActionListener>()
    private val properties = ArrayList<GameProperty>()
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

    private var inited = false
    fun init() {
        if (inited) return

        onInit.forEach { it.run() }
        onLoad.forEach { it.run() }
        inited = true
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun next() {
        next0()
        nextTime()
    }

    private fun next0() {
        if (object_time == 0) onBirth.forEach { it.run() }

        bounds.next()

        controllers.forEach { it.update() }

        abilities.forEach { it.run() }

        properties.forEach { it.update() }

        effects.forEach { it.update() }
        effects.removeIf { it.dead }

        setMain()
    }

    var main: RectN? = null
        private set

    private fun setMain() {
        val bsl = ArrayList<RectN>()
        for (bc in BoundsBox.indices) {
            bounds[bc].main?.let { bsl.add(it) }
        }
        main = if (bsl.isEmpty()) null
        else ShapeUtils.mainOf(*bsl.toTypedArray())
    }

    private fun nextTime() {
        object_time++
    }

    internal fun nextWithFollowers() {
        next()
        followers.removeIf { it.dead }

        followers.forEach { it.nextWithFollowers() }
    }

    internal fun next0WithFollowers() {
        next0()
        followers.removeIf { it.dead }

        followers.forEach { it.next0WithFollowers() }
    }

    internal fun nextTimeWithFollowers() {
        nextTime()

        followers.forEach { it.nextTimeWithFollowers() }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun draw(draw_pos: PointN) {
        visuals.sortBy { v -> v.drawLayer().sort }
        visuals.forEach { it.drawWithDefaults(draw_pos) }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    fun addEffect(vararg effect: TagEffect) = effects.addAll(effect)
    fun effected(effect: String) = effects.any { it.name == effect }
    fun effectedAny(vararg effect: String) = effect.any { effected(it) }
    fun effectedAll(vararg effect: String) = effect.all { effected(it) }

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    final override fun hashCode(): Int {
        return super.hashCode()
    }

    final override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////// Component Functionality ////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addBounds(bs: GroupBounds) = addComponent(bs)

    fun addAbility(ability: GroupAbility) = addComponent(ability)
    fun addController(controller: GroupController) = addComponent(controller)
    fun addListener(listener: GroupListener) = addComponent(listener)
    fun addProperty(property: GroupProperty) = addComponent(property)
    fun addVisual(vis: GroupVisualiser) = addComponent(vis)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addBounds(code: CODE, vararg bs: BoundsElement) {
        bs.forEach {
            addComponent(BoundsComponent(code, it))
        }
    }

    fun addBounds(code: CODE, shape: () -> Shape?) {
        addComponent(BoundsComponent(code, BoundsElement(shape)))
    }

    fun addBounds(code: CODE, name: String, shape: () -> Shape?) {
        addComponent(BoundsComponent(code, BoundsElement(name, shape)))
    }

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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addController(controller: () -> (() -> TempAction)) = addComponent(Controller(controller))
    fun addController(vararg controller: Controller) = addComponent(*controller)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addListener(listener: (InputAction) -> Unit) = addComponent(ActionListener(listener))
    fun addListener(vararg listener: ActionListener) = addComponent(*listener)
    fun addListener(code: String, f: (action: InputAction) -> Unit) {
        addListener { action ->
            if (action.code == code) f(action)
        }
    }

    fun addBoundsListener(
        code: String,
        our: String? = null,
        their: String? = null,
        f: (action: BoundsInputAction) -> Unit
    ) {
        addListener { action ->
            if (action.code != code) return@addListener
            if (action.args.size != 2) throw DebugData.error("Can't cast to BoundsInputAction: $action")

            val bounds_action = BoundsInputAction(action)
            if (our != null && our != bounds_action.our) return@addListener
            if (their != null && their != bounds_action.their) return@addListener

            f(bounds_action)
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addAbility(ability: () -> Unit) = addComponent(AbilityBox(ability))
    fun addAbility(vararg ability: AbilityBox) = addComponent(*ability)

    fun addProperty(vararg property: GameProperty) = addComponent(*property)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addVisual(vararg vis: Visualiser) = addComponent(*vis)

    fun addVisual(layer: DrawLayer, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        addComponent(
            object: LayerVisualiser(layer) {
                override fun draw(draw_pos: PointN) {
                    vis(agc, draw_pos)
                }
            }
        )
    }

    fun addVisual(layer: DrawLayer, sort_pos: PointN, vis: (agc: AffineGraphics, draw_pos: PointN) -> Unit) {
        addComponent(
            object: LayerVisualiser(layer) {
                init {
                    sortPOS = sort_pos
                }

                override fun draw(draw_pos: PointN) {
                    vis(agc, draw_pos)
                }
            }
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun addImage(name: String, vararg effects: String) = addComponent(ImageResource(name, *effects))
    fun addImage(name: String, size: IntI, vararg effects: String) = addComponent(SpriteResource(name, size, *effects))

    fun addAudio(name: String) = addComponent(AudioResource(name))

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun onInit(f: () -> Unit) = addComponent(OnInitComponent(f))
    fun onInit(f: OnInitComponent) = addComponent(f)

    fun onLoad(f: () -> Unit) = addComponent(OnLoadComponent(f))
    fun onLoad(f: OnLoadComponent) = addComponent(f)

    fun onBirth(f: () -> Unit) = addComponent(OnBirthComponent(f))
    fun onBirth(f: OnBirthComponent) = addComponent(f)

    fun onDeath(f: () -> Unit) = addComponent(OnDeathComponent(f))
    fun onDeath(f: OnDeathComponent) = addComponent(f)

    fun onGrab(f: () -> Unit) = addComponent(OnGrabComponent(f))
    fun onGrab(f: OnGrabComponent) = addComponent(f)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
