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
        val filename = "map|tiles.png"
        Data.set(filename, IntI(16, 16))
        visuals.add(object: LayerVisualiser(Game.layer("OBJ")) {
            override fun draw(draw_pos: PointN) {
                agc().image.drawC(Data.get(filename, IntI(7, 2)), draw_pos)
            }
        })
        bounds.red = { Bounds(RectN(-Game.STEP*8, Game.STEP*16)) }
    }

    override fun setValues() {
        name = "wall"
        values.add(PosValue(stats.POS))
    }
}
