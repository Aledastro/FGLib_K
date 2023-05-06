package com.uzery.fglib.extension.event

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

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
        abilities.add(object: AbilityBox {
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
        })
        visuals.add(object: LayerVisualiser(DrawLayer(0.0, 3.0)) {
            override fun draw(draw_pos: PointN) {
                agc().fill.oval(
                    draw_pos - PointN(10, 10),
                    PointN(20, 20), FGUtils.transparent(Color.CORAL, 0.9))
                agc().fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 15.0)
                agc().fill.textC(draw_pos + PointN(0, 6), "E", Color.WHITE)
            }
        })
    }

    open fun activate0(action: InputAction) {
        /* #ignore */
    }
}
