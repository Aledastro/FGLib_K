import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.modificator.Modificator
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.scale.AnimationScale
import com.uzery.fglib.utils.math.scale.GradientScale
import com.uzery.fglib.utils.math.value.PosValue
import com.uzery.fglib.utils.math.value.SizeValue
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color

class Player(pos: PointN): GameObject() {
    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun next() {
                if(keyboard.pressed(KeyCode.W)) stats.POS -= PointN(0.0, 2.0)
                if(keyboard.pressed(KeyCode.S)) stats.POS += PointN(0.0, 2.0)
                if(keyboard.pressed(KeyCode.A)) stats.POS -= PointN(2.0, 0.0)
                if(keyboard.pressed(KeyCode.D)) stats.POS += PointN(2.0, 0.0)
                if(mouse_keys.pressed(MouseButton.PRIMARY)) {
                    stats.POS = mouse.pos()
                }
            }
        }
        visuals.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 0.5, 1.0)
                agc().fill.rect(pos, PointN(24.0, 24.0))
            }

            override fun drawLayer(): DrawLayer {
                val scale = AnimationScale(0L, 60.0) { k -> k/2 }
                return DrawLayer(scale.swing(object_time))
            }
        })
        orangeBounds = { Bounds(RectN(PointN(0.0, 0.0), PointN(24.0, 24.0))) }
        modificators.add(object: Modificator {
            override fun update() {
                //stats.POS += PointN(0.0, 0.1)
            }
        })
    }

    var temp1: () -> TempAction = {
        object: TempAction {
            var t = 0
            override fun next() {
                t++
            }

            override val ends: Boolean
                get() = t>5

        }
    }
    var temp2: () -> TempAction = {
        object: TempAction {
            var t = 0

            override fun next() {
                t++
            }

            override val ends: Boolean
                get() = t>5
        }
    }

    override fun setValues() {
        name="player"
        values.add(PosValue(stats.POS))
        values.add(SizeValue(stats.POS))
    }
}