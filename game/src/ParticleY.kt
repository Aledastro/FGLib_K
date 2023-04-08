import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.paint.Color

class ParticleY(pos: PointN, private var layer: Double): GameObject() {
    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun next() {
                if(Math.random()<0.005) produce(ParticleY(PointN(stats.POS), layer))
                if(object_time>100) stats.dead = true
                layer += (Math.random() - 0.5)*0.1
            }
        }
        controller = object: Controller {
            override fun get(): () -> TempAction {
                if(Math.random()>0.5) return temp1
                return temp2
            }
        }
        visuals.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                DrawLayer(layer)
                agc().fill.color = Color(1.0, 0.0, 1.0, 0.1)
                agc().fill.oval(pos, PointN(12.0, 12.0))
            }

            override fun drawLayer(): DrawLayer = DrawLayer(layer)
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

    override fun setValues() {
        name="temp"
    }
}