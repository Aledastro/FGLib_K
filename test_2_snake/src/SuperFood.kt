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

class SuperFood(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code==InputAction.CODE.INTERACT){
                    stats.dead=true
                }
            }
            override fun run() { /* ignore */
            }
        }
        visuals.add(object: Visualiser() {
            val scale = AnimationScale(0L, 30.0) { x -> 8*(x + 0.5) }

            override fun draw(pos: PointN) {
                var p=Game.STEP*scale.swing(object_time)
                agc().fill.oval(pos - p, p*2, Color(0.7, 0.0, 0.2, 1.0))

                p*=1.4

                agc().setStroke(3.0)
                agc().stroke.oval(pos - p, p*2, Color(0.7, 0.0, 0.2, 1.0))
            }

            override fun drawLayer(): DrawLayer {
                val scale = AnimationScale(0L, 30.0) { k -> k/2 }
                return DrawLayer(scale.swing(object_time))
            }
        })
        bounds.green = { Bounds(RectN(-Game.STEP*20, Game.STEP*40)) }
    }
}