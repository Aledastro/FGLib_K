package com.uzery.fglib.ext.event

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction

abstract class GameEvent: GameObject() {
    var repeatable = false
        protected set

    var finished = false
        private set

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
                        collapse()
                    }
                }
            }
        }
    }

    open fun activate0(action: InputAction) {
        /* #ignore */
    }
}
