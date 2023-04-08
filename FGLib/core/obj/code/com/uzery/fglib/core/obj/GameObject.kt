package com.uzery.fglib.core.obj

import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.modificator.Modificator
import com.uzery.fglib.core.obj.stats.Stats
import java.util.*

abstract class GameObject {
    var stats = Stats()
    var abilityBox: AbilityBox? = null
    var controller: Controller? = null
    var visual = ArrayList<Visualiser>()
    var modificators = LinkedList<Modificator>()

    var redBounds: (() -> Bounds?)? = null
    var orangeBounds: (() -> Bounds)? = null
    var blueBounds: (() -> Bounds?)? = null
    var greenBounds: (() -> Bounds?)? = null

    private var temp: TempAction? = null
    internal var children = ArrayList<GameObject>()


    fun next() {
        if(temp == null || temp!!.ends) temp = controller?.get()?.invoke()
        temp?.next()
        abilityBox?.next()
        modificators.forEach { m -> m.update() }

        stats.life++
    }

    fun draw() {
        visual.forEach { v -> v.draw(stats.POS) }
    }

    fun produce(o: GameObject) = children.add(o)
}
