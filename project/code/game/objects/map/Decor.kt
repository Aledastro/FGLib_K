package game.objects.map

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.utils.data.image.Data
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.value.DrawLayerValue
import com.uzery.fglib.utils.math.getter.value.IntIValue
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.num.IntI
import game.Game

class Decor(pos: PointN, var filename: String, var set: IntI, var get: IntI, var layer: DrawLayer): GameObject() {
    init {
        if(filename == "") {
            filename = "map|tiles.png"
            set = IntI(16, 16)
            get = IntI(0, 0)
            layer = Game.layer("DRT")
            //todo
        }
        stats.POS = pos
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT) {
                    collapse()
                }
            }
        })
        Data.set(filename, set)
        visuals.add(object: LayerVisualiser(layer) {
            override fun draw(draw_pos: PointN) {
                agc().image.drawC(Data.get(filename, get), draw_pos)
            }
        })
        tag("#inactive", "#immovable")
    }

    override fun setValues() {
        name = "decor"
        values.add(PosValue(stats.POS))
        values.add(filename)
        values.add(IntIValue(set))
        values.add(IntIValue(get))
        values.add(DrawLayerValue(layer))
    }
}
