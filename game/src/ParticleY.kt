import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.Visualiser
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.paint.Color

class ParticleY(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun next() {
                if(Math.random()<0.005) produce(ParticleY(PointN(stats.POS)))
                if(stats.life>100) stats.dead = true
            }
        }
        controller = object: Controller {
            override fun get(): () -> TempAction {
                if(Math.random()>0.5) return temp1
                return temp2
            }
        }
        visual.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 1.0, 0.1)
                agc().fill.rect(pos, PointN(12.0, 12.0))
            }
        })

        orangeBounds = { Bounds(RectN(PointN(0.0, 0.0), PointN(1.0, 1.0))) }
    }

    var temp1: () -> TempAction = {
        object: TempAction {
            var t = 0
            override fun next() {
                stats.POS += PointN(1.0, 0.0)
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
                stats.POS += PointN(0.0, 1.0)
                t++
            }

            override val ends: Boolean
                get() = t>5
        }
    }
}