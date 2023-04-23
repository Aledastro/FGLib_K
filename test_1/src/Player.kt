import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.modificator.Modificator
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.scale.AnimationScale
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color

class Player(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun run() {
                if(keyboard.pressed(KeyCode.W)) stats.nPOS -= Game.Y*2
                if(keyboard.pressed(KeyCode.S)) stats.nPOS += Game.Y*2
                if(keyboard.pressed(KeyCode.A)) stats.nPOS -= Game.X*2
                if(keyboard.pressed(KeyCode.D)) stats.nPOS += Game.X*2
                if(mouse_keys.pressed(MouseButton.PRIMARY)) {
                    stats.POS = mouse.pos()
                }
            }

            override fun activate(a: InputAction) { /* ignore */
            }
        }
        visuals.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 0.5, 1.0)
                agc().image.draw("images/player.png", pos - Game.STEP*36)
            }

            override fun drawLayer(): DrawLayer {
                val scale = AnimationScale(0L, 60.0) { k -> k/2 }
                return DrawLayer(scale.swing(object_time))
            }
        })
        bounds.red = { Bounds(RectN(-Game.STEP*62, Game.STEP*124)) }
        modificators.add(object: Modificator {
            override fun update() {

            }
        })
    }
    override fun setValues() {
        name = "player"
        values.add(PosValue(stats.POS))
    }
}