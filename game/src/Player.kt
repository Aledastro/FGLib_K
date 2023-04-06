import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.Visualiser
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color

class Player(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun next() {
                println("${stats.POS.X()} ${stats.POS.Y()}")
            }
        }
        visual.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 1.0, 1.0)
                agc().fill.rect(pos, PointN(12.0, 12.0))
            }
        })
        controller = object: Controller {
            override fun get(): () -> TempAction {
                if(Math.random()>0.5) return temp1
                return temp2
            }
        }
    }

    var temp1: () -> TempAction = {
        object: TempAction {
            var t = 0
            override fun next() {
                stats.POS.xs[0]++
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
                stats.POS.xs[1]++
                t++
            }

            override val ends: Boolean
                get() = t>5
        }
    }
}