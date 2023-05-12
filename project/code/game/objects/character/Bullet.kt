package game.objects.character

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.scale.AnimationScale
import game.Game
import javafx.scene.paint.Color

class Bullet(pos: PointN, private val speed: PointN): GameObject() {
    init {
        stats.POS = pos
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT) {
                    if(action.prime.tagged("bullet_go")) return
                    action.prime.activate(InputAction(InputAction.CODE.DAMAGE, "bullet", this@Bullet))
                    collapse()
                }
            }

            override fun run() {
                stats.POS += speed
                if(object_time>100) collapse()
            }
        })
        visuals.add(object: LayerVisualiser(Game.layer("OBJ-")) {
            val scale = AnimationScale(0L, 30.0) { x -> 4*(x + 0.5) }

            override fun draw(draw_pos: PointN) {
                agc().fill.oval(
                    draw_pos - Game.STEP*scale.swing(object_time),
                    Game.STEP*2*scale.swing(object_time),
                    Color(0.9, 0.9, 0.9, 1.0))
            }
        })
        bounds.blue = { Bounds(RectN(-Game.STEP*3, Game.STEP*6)) }
    }
}
