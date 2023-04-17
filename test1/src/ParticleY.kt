import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.paint.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class ParticleY(pos: PointN, private var layer: Double): GameObject() {

    companion object {
        var st = 0
    }

    val alpha = st*0.0003

    //Math.random()*2*PI
    init {
        st++
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun run() {
                if(Math.random()<0.0005) produce(ParticleY(PointN(stats.POS), layer))
                if(object_time>1000) stats.dead = true
                layer += (Math.random() - 0.5)*0.1
                if((stats.lPOS - stats.POS).length()<1) object_time += 5
            }

            override fun activate(a: InputAction) { /* ignore */
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
                agc().fill.oval(pos - Game.STEP*6, Game.STEP*12, Color(1.0, 1.0, 1.0, 0.05))
                agc().fill.oval(pos - Game.STEP*5, Game.STEP*10, Color(1.0, 1.0, 1.0, 0.1))
                agc().fill.oval(pos - Game.STEP*3, Game.STEP*6, Color(1.0, 1.0, 1.0, 0.2))
            }

            override fun drawLayer(): DrawLayer = DrawLayer(layer)
        })

        orangeBounds = { Bounds(RectN(-Game.STEP*3, Game.STEP*6)) }
    }

    var temp1: () -> TempAction = {
        object: TempAction {
            var t = 0
            override fun next() {
                val r = Math.random()
                stats.nPOS += Game.X*r*cos(alpha)*10
                stats.nPOS += Game.Y*r*sin(alpha)*10
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
                val r = Math.random()
                stats.nPOS += Game.X*r*cos(alpha)*10
                stats.nPOS += Game.Y*r*sin(alpha)*10
                t++
            }

            override val ends: Boolean
                get() = t>5
        }
    }

    override fun setValues() {
        name = "temp"
    }
}