package com.uzery.fglib.core.obj

import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.modificator.Modificator
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.math.geom.PointN
import java.util.*

abstract class GameObject {
    val stats = Stats()
    val visuals = ArrayList<Visualiser>()
    val modificators = LinkedList<Modificator>()
    val bounds = BoundsBox()
    val abilities = LinkedList<AbilityBox>()

    val children = ArrayList<GameObject>()
    val grabbed = LinkedList<GameObject>()
    var owner: GameObject? = null

    var controller: Controller? = null
    private var temp: TempAction? = null


    var dead = false
        private set

    var object_time = 0
        private set
    var name = "temp"
    val values = ArrayList<Any>()

    fun next() {
        if(object_time == 0) afterInit()
        if(temp == null || temp!!.ends) temp = controller?.get()?.invoke()
        temp?.next()
        abilities.forEach { it.run() }
        modificators.forEach { it.update() }
        effects.forEach { it.update() }
        effects.removeIf { it.dead }

        object_time++
    }

    open fun afterInit() {
        /* ignore */
    }

    fun draw(draw_pos: PointN) = visuals.forEach { it.draw(draw_pos) }

    protected fun produce(vararg os: GameObject) = children.addAll(os)
    protected fun produce(os: List<GameObject>) = children.addAll(os)

    fun grab(vararg os: GameObject) {
        grabbed.addAll(os)
        os.forEach { it.owner = this }
        os.forEach { it.onGrab() }
    }

    fun grab(os: List<GameObject>) {
        grabbed.addAll(os)
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
        if(values.isNotEmpty()) {
            s.append(":")
            values.forEach { value ->
                val ss = value.toString()
                if(ss[ss.lastIndex] == ']') s.append(" $ss")
                else s.append(" [$ss]")
            }
        }
        return s.toString()
    }

    fun activate(action: InputAction) {
        abilities.forEach { a -> a.activate(action) }
        temp?.activate(action)
    }

    open fun interact() = false

    fun collapse() {
        onDeath()
        dead = true
    }

    open fun onDeath() {
        /* ignore */
    }

    open fun onGrab() {
        /* ignore */
    }


    private val tags = LinkedList<String>()
    fun tag(vararg tag: String) = tags.addAll(tag)
    fun tagged(tag: String) = tags.contains(tag)

    private val effects = LinkedList<TagEffect>()
    fun addEffect(vararg effect: TagEffect) = effects.addAll(effect)
    fun effected(effect: String) = effects.any { a -> a.name == effect }
    fun effectedAny(vararg effect: String) = effect.any { eff -> effected(eff) }
    fun effectedAll(vararg effect: String) = effect.all { eff -> effected(eff) }
}
