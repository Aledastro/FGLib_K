package game.objects.map

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.scale.AnimationScale
import game.Game
import javafx.scene.paint.Color

class Wall(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT) {
                    collapse()
                }
            }
        })
        visuals.add(object: Visualiser {
            val scale = AnimationScale(0L, 30.0) { x -> 2*(x + 6) }

            override fun draw(draw_pos: PointN) {
                agc().fill.rect(
                    draw_pos - Game.STEP*scale.swing(object_time),
                    Game.STEP*2*scale.swing(object_time),
                    Color(0.2, 0.2, 0.2, 1.0))
            }

            override fun drawLayer(): DrawLayer {
                val scale = AnimationScale(0L, 60.0) { k -> k/2 }
                return DrawLayer(1.0, scale.swing(object_time))
            }
        })
        bounds.red = { Bounds(RectN(-Game.STEP*16, Game.STEP*32)) }
    }

    override fun setValues() {
        name = "wall"
        values.add(PosValue(stats.POS))
    }
}
