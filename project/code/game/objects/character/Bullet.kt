package game.objects.character

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.utils.data.image.Data
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.num.IntI
import game.Game

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
        val filename = "char|bullets.png"
        Data.set(filename, IntI(4, 4), 2)
        visuals.add(object: LayerVisualiser(Game.layer("OBJ-")) {
            override fun draw(draw_pos: PointN) {
                agc().image.draw(Data.get(filename, IntI(0, 0)), draw_pos - Game.STEP*3)
            }
        })
        bounds.blue = { Bounds(RectN(-Game.STEP*3, Game.STEP*6)) }
    }
}
