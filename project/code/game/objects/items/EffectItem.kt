package game.objects.items

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.TagEffect
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.utils.data.image.Data
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.num.IntI
import game.Game

open class EffectItem(pos: PointN, id: Int, private val effect_name: String, private val duration: Int): GameObject() {
    init {
        stats.POS = pos

        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.INTERRUPT && action.prime.tagged("player")) {
                    action.prime.grab(this@EffectItem)
                }
            }

            override fun run() {

            }
        })
        val filename = "item|items.png"
        Data.set(filename, IntI(16, 16), 2)
        visuals.add(object: LayerVisualiser(DrawLayer.CAMERA_FOLLOW) {
            override fun draw(draw_pos: PointN) {
                agc().image.draw(Data.get(filename, IntI(id, 0)), draw_pos - Game.STEP*16)
            }
        })

        bounds.blue = { Bounds(RectN(-Game.STEP*16, Game.STEP*32)) }
    }

    final override fun onGrab() {
        owner?.addEffect(TagEffect(effect_name, duration))
    }
}
