import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.modificator.Modificator
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.scale.AnimationScale
import javafx.scene.paint.Color

class Food(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun run() { /* ignore */
            }

            override fun activate(a: InputAction) { /* ignore */
            }
        }
        visuals.add(object: Visualiser() {
            val scale = AnimationScale(object_time, 30.0) { x -> 4*x + 2 }

            override fun draw(pos: PointN) {
                agc().fill.oval(
                    pos - Game.STEP*scale.swing(object_time),
                    Game.STEP*2*scale.swing(object_time),
                    Color(1.0, 0.0, 0.5, 1.0))
            }

            override fun drawLayer(): DrawLayer {
                val scale = AnimationScale(0L, 60.0) { k -> k/2 }
                return DrawLayer(scale.swing(object_time))
            }
        })
        bounds.red = { Bounds(RectN(-Game.STEP*20, Game.STEP*40)) }
        modificators.add(object: Modificator {
            override fun update() {

            }
        })
    }

    var temp1: () -> TempAction = {
        object: TempAction {
            var t = 0
            override fun next() {
                t++
            }

            override val ends: Boolean
                get() = t>5

        }
    }
    var temp2: () -> TempAction = {
        object: TempAction {
            var t = 0

            override fun next() {
                t++
            }

            override val ends: Boolean
                get() = t>5
        }
    }

    override fun setValues() {
        name = "player"
        values.add(PosValue(stats.POS))
    }
}