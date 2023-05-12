package game.objects.map

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.utils.data.image.Data
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.num.IntI
import game.Game

class Cactus(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT) {
                    collapse()
                }
            }
        })
        val filename = "map|tiles.png"
        Data.set(filename, IntI(16, 16), 2)
        visuals.add(object: LayerVisualiser(Game.layer("OBJ")) {
            override fun draw(draw_pos: PointN) {
                agc().image.draw(Data.get(filename, IntI(if(object_time%80<40) 5 else 6, 2)), draw_pos - Game.STEP*16)
            }
        })
        bounds.red = { Bounds(RectN(-Game.STEP*16, Game.STEP*32)) }
        tag("bullet_go")
    }

    override fun setValues() {
        name = "cactus"
        values.add(PosValue(stats.POS))
    }
}
