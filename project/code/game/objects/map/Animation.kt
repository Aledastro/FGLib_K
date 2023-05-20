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

open class Animation(
    pos: PointN,
    var filename: String,
    var set: IntI,
    var get: IntI,
    var layer: DrawLayer,
    var length: Int,
    var frames: Double,
): GameObject() {
    var progress = 0

    init {
        if(filename == "") {
            filename = "map|tiles.png"
            set = IntI(16, 16)
            get = IntI(0, 0)
            layer = Game.layer("DRT")
            length = 1
            frames = 10.0
            //todo
        }
        stats.POS = pos
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT) {
                    collapse()
                }
            }

            override fun run() {
                //todo
                progress = (object_time/frames*length).toInt()
                if(progress == length) progress--

                if(object_time>=frames) collapse()
            }
        })
        Data.set(filename, set)
        visuals.add(object: LayerVisualiser(layer) {
            override fun draw(draw_pos: PointN) {
                agc().image.draw(Data.get(filename, IntI(get.n + progress, get.m)), draw_pos - PointN(set)/2)
            }
        })
        tag("#inactive")
    }

    override fun setValues() {
        name = "animation"
        values.add(PosValue(stats.POS))
        values.add(filename)
        values.add(IntIValue(set))
        values.add(IntIValue(get))
        values.add(DrawLayerValue(layer))
        values.add(length)
        values.add(frames)
    }
}
