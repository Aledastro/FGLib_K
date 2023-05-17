package game.objects.map

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import game.Game

class BlackBox(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT) {
                    collapse()
                }
            }
        })
        bounds.red = { Bounds(RectN(-Game.STEP*8, Game.STEP*16)) }
        tag("#immovable", "bullet_go")
    }

    override fun setValues() {
        name = "black_box"
        values.add(PosValue(stats.POS))
    }
}
