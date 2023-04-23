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
import com.uzery.fglib.utils.math.getter.value.ObjectValue
import java.util.*

abstract class GameObject {
    val stats = Stats()
    val visuals = ArrayList<Visualiser>()
    val modificators = LinkedList<Modificator>()
    val bounds = BoundsBox()
    val children = ArrayList<GameObject>()

    var abilityBox: AbilityBox? = null
    var controller: Controller? = null
    private var temp: TempAction? = null


    var dead = false
        private set

    var object_time = 0
        private set
    var name = "temp"
    val values = ArrayList<ObjectValue>()

    fun next() {
        if(temp == null || temp!!.ends) temp = controller?.get()?.invoke()
        temp?.next()
        abilityBox?.run()
        modificators.forEach { m -> m.update() }

        object_time++
    }

    fun draw(pos: PointN) = visuals.forEach { v -> v.draw(pos) }

    protected fun produce(o: GameObject) = children.add(o)
    open fun setValues() {
        name = "temp"
    }

    override fun toString(): String {
        values.clear()
        setValues()
        val s = StringBuilder(name)
        if(values.isNotEmpty()) {
            s.append(":")
            values.forEach { value -> s.append(" $value") }
        }
        return s.toString()
    }

    fun activate(action: InputAction) {
        abilityBox?.activate(action)
        temp?.activate(action)
    }

    open fun interact() = false

    fun collapse() {
        dead = true
    }


    private val tags=LinkedList<String>()
    fun tag(vararg tag: String) = tags.addAll(tag)
    fun tagged(tag: String) = tags.contains(tag)

}
