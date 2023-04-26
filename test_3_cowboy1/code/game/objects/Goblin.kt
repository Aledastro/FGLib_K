package game.objects

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.controller.TimeTempAction
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.scale.AnimationScale
import game.Game
import javafx.scene.paint.Color
import kotlin.math.cos
import kotlin.math.sin

class Goblin(pos: PointN): GameObject() {

    var LIFE = 4

    init {

        stats.POS = pos
        controller = object: Controller {
            override fun get(): () -> TempAction {
                if(Math.random()>0.5) return attack
                return stay
            }
        }
        abilityBox = object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT_I) {
                    LIFE -= 2
                }
            }

            override fun run() {
                if(LIFE<=0) collapse()
            }
        }
        visuals.add(object: Visualiser() {
            val scale = AnimationScale(0L, 30.0) { x -> 4*(x + 1.5) }

            override fun draw(pos: PointN) {
                agc().fill.rect(
                    pos - Game.STEP*scale.swing(object_time),
                    Game.STEP*2*scale.swing(object_time),
                    Color(0.7, 0.0, 0.2, 1.0))
            }

            override fun drawLayer(): DrawLayer {
                val scale = AnimationScale(0L, 60.0) { k -> k/2 }
                return DrawLayer(scale.swing(object_time))
            }
        })
        bounds.orange = { Bounds(RectN(-Game.STEP*12, Game.STEP*24)) }
    }

    var attack: () -> TempAction = {
        object: TimeTempAction() {
            var goal: GameObject? = null
            override fun start() {
                goal = Game.allTagged("player").findFirst().get()
            }

            override fun update() {
                val g = goal ?: throw IllegalArgumentException()
                val d = MathUtils.getDegree(stats.POS, g.stats.POS)
                stats.nPOS += Game.X*cos(d)*1.2
                stats.nPOS += Game.Y*sin(d)*1.2
            }

            override fun ends() = false
        }
    }

    var stay: () -> TempAction = {
        object: TempAction {
            var t = 0

            override fun next() {
                val r = Math.random()
                stats.nPOS -= Game.X*r*2
                stats.nPOS -= Game.Y*r*2
                t++
            }

            override val ends: Boolean
                get() = t>5
        }
    }
}
