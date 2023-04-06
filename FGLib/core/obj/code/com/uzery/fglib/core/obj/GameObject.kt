package com.uzery.fglib.core.obj

import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.stats.Stats

open class GameObject {
    var stats = Stats()
    var abilityBox: AbilityBox? = null
    var controller: Controller? = null
    var visual: ArrayList<Visualiser> = ArrayList()

    private var temp: TempAction? = null


    fun next() {
        abilityBox?.next()
        if(temp == null || temp!!.ends) temp = controller?.get()?.invoke()
        temp?.next()
    }

    fun draw() {
        visual.forEach { v -> v.draw(stats.POS) }
    }
}
