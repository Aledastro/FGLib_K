package com.uzery.fglib.core.obj

import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.bounds.Bounds
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
    var stats = Stats()
    var abilityBox: AbilityBox? = null
    var controller: Controller? = null
    var visuals = ArrayList<Visualiser>()
    var modificators = LinkedList<Modificator>()
    var bounds = BoundsBox()

    private var temp: TempAction? = null
    internal var children = ArrayList<GameObject>()

    var object_time = 0L
    var name: String = "temp"
    var values = ArrayList<ObjectValue>()

    fun next() {
        if(temp == null || temp!!.ends) temp = controller?.get()?.invoke()
        temp?.next()
        abilityBox?.run()
        modificators.forEach { m -> m.update() }

        object_time++
    }

    fun draw(pos: PointN) = visuals.forEach { v -> v.draw(pos) }

    fun produce(o: GameObject) = children.add(o)
    abstract fun setValues()

    override fun toString(): String {
        values.clear()
        setValues()
        val s = StringBuilder(name)
        if(values.isNotEmpty()) {
            s.append(":")
            values.forEach { value -> s.append(" ").append(value) }
        }
        return s.toString()
    }
}
