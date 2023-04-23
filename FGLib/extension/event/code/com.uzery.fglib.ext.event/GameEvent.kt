package com.uzery.fglib.ext.event

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction

abstract class GameEvent: GameObject() {
    protected var repeatable = false
    var finished = false
    abstract fun ready(): Boolean
    abstract fun start()
    abstract fun update()
    abstract fun finish()
    abstract fun ends(): Boolean

    private var init = false

    init {
        abilityBox = object: AbilityBox {
            override fun activate(action: InputAction) = activate0(action)

            override fun run() {
                if(!init) {
                    if(ready()) {
                        start()
                        init = true
                    } else return
                }
                update()
                if(ends()) {
                    finish()
                    if(repeatable) {
                        init = false
                    } else {
                        finished = true
                        stats.dead = true
                    }
                }
            }
        }
    }

    fun activate0(a: InputAction) {  /* #ignore */
    }
}
