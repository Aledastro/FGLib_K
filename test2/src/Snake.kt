import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.modificator.Modificator
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.scale.AnimationScale
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import java.util.*

class Snake(pos: PointN): GameObject() {
    private enum class Direct {
        UP, DOWN, LEFT, RIGHT
    }

    private var direct = Direct.UP
    private var nPOS = PointN.ZERO
    private var body = LinkedList<PointN>()

    init {
        stats.POS = pos
        body.add(stats.POS)
        abilityBox = object: AbilityBox {
            override fun run() {
                if(direct == Direct.UP || direct == Direct.DOWN) {
                    if(keyboard.pressed(KeyCode.A)) {
                        nPOS = -Game.X*2
                        direct = Direct.LEFT
                    }
                    if(keyboard.pressed(KeyCode.D)) {
                        nPOS = Game.X*2
                        direct = Direct.RIGHT
                    }
                }
                if(direct == Direct.LEFT || direct == Direct.RIGHT) {
                    if(keyboard.pressed(KeyCode.W)) {
                        nPOS = -Game.Y*2
                        direct = Direct.UP
                    }
                    if(keyboard.pressed(KeyCode.S)) {
                        nPOS = Game.Y*2
                        direct = Direct.DOWN
                    }
                }
                stats.nPOS += nPOS
                body.addLast(stats.POS)
                body.removeFirst()
            }

            override fun activate(a: InputAction) { /* ignore */
            }
        }
        visuals.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 0.5, 1.0)
                body.forEach { p -> agc().image.draw("images/player.png", p - Game.STEP*36) }
            }

            override fun drawLayer(): DrawLayer {
                val scale = AnimationScale(0L, 60.0) { k -> k/2 }
                return DrawLayer(scale.swing(object_time))
            }
        })
        bounds.blue = { Bounds(RectN(-Game.STEP*20, Game.STEP*40)) }
        modificators.add(object: Modificator {
            override fun update() {

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
        name = "player"
        values.add(PosValue(stats.POS))
    }
}