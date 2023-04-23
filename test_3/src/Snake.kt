import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.utils.data.image.Data
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

    private var grow=false

    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun run() {
                if(object_time%12!=0L)return
                if(direct == Direct.UP || direct == Direct.DOWN) {
                    if(keyboard.pressed(KeyCode.A)) {
                        nPOS = -Game.X*40
                        direct = Direct.LEFT
                    }
                    if(keyboard.pressed(KeyCode.D)) {
                        nPOS = Game.X*40
                        direct = Direct.RIGHT
                    }
                }
                if(direct == Direct.LEFT || direct == Direct.RIGHT) {
                    if(keyboard.pressed(KeyCode.W)) {
                        nPOS = -Game.Y*40
                        direct = Direct.UP
                    }
                    if(keyboard.pressed(KeyCode.S)) {
                        nPOS = Game.Y*40
                        direct = Direct.DOWN
                    }
                }
                stats.nPOS = nPOS
                body.addLast(stats.POS)
                if(grow) grow=false
                else body.removeFirst()
            }

            override fun activate(action: InputAction) {
                if(action.code==InputAction.CODE.INTERRUPT_I){
                    grow=true
                }
                if(action.code==InputAction.CODE.INTERACT_I){
                    grow=true
                }
            }
        }
        visuals.add(object: Visualiser() {
            val scale = AnimationScale(0L, 30.0) { x -> 6*(x + 2) }
            val scaleL = AnimationScale(0L, 60.0) { k -> k/2 }

            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 0.5, 1.0)
                /*agc().image.draw(Data.get("images/player.png"), stats.POS - Game.STEP*36)
                body.forEach { p -> agc().image.draw(Data.get("images/player.png"), p - Game.STEP*36) }*/
                body.forEach { p -> agc().fill.rect(
                    p - Game.STEP*scale.swing(object_time),
                    Game.STEP*2*scale.swing(object_time),
                    Color(0.3, 0.5, 0.2, 1.0))
                }
                agc().fill.rect(
                    pos - Game.STEP*scale.swing(object_time),
                    Game.STEP*2*scale.swing(object_time),
                    Color(0.4, 0.7, 0.15, 1.0))
                agc().fill.rect(
                    pos - PointN(3.0,2.0)*0.15*scale.swing(object_time),
                    PointN(1.0,2.0)*0.3*scale.swing(object_time),
                    Color(0.1, 0.1, 0.1, 1.0))
                agc().fill.rect(
                    pos - PointN(-1.0,2.0)*0.15*scale.swing(object_time),
                    PointN(1.0,2.0)*0.3*scale.swing(object_time),
                    Color(0.1, 0.1, 0.1, 1.0))
            }

            override fun drawLayer() = DrawLayer(scaleL.swing(object_time))
        })
        bounds.orange = { Bounds(RectN(-Game.STEP*20, Game.STEP*40)) }
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

    override fun interact() = keyboard.pressed(KeyCode.E)
}