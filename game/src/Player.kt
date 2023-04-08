import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.Visualiser
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.modificator.Modificator
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.mouse
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
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
        visual.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 0.5, 0.1)
                agc().fill.rect(pos, PointN(24.0, 24.0))
            }
        })
        orangeBounds = { Bounds(RectN(PointN(0.0, 0.0), PointN(24.0, 24.0))) }
        modificators.add(object: Modificator {
            override fun update() {
                stats.POS += PointN(0.0, 0.1)
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
}