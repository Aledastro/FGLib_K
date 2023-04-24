package game.objects

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.scale.AnimationScale
import game.Game
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.paint.Color

class Cowboy(pos: PointN): GameObject() {
    var ready_shoot = 0

    init {
        stats.POS = pos
        abilityBox = object: AbilityBox {
            override fun run() {
                if(keyboard.pressed(KeyCode.W)) stats.nPOS -= Game.Y*2.4
                if(keyboard.pressed(KeyCode.S)) stats.nPOS += Game.Y*2.4
                if(keyboard.pressed(KeyCode.A)) stats.nPOS -= Game.X*2.4
                if(keyboard.pressed(KeyCode.D)) stats.nPOS += Game.X*2.4
                if(Platform.mouse_keys.pressed(MouseButton.PRIMARY)) {
                    stats.POS = Platform.mouse.pos()
                }
                if(ready_shoot<0 && keyboard.anyPressed(KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT)) {
                    val s = 8.0
                    ready_shoot = 7
                    var ix = 0
                    var iy = 0
                    when {
                        keyboard.pressed(KeyCode.UP) -> iy--
                        keyboard.pressed(KeyCode.DOWN) -> iy++
                    }
                    when {
                        keyboard.pressed(KeyCode.LEFT) -> ix--
                        keyboard.pressed(KeyCode.RIGHT) -> ix++
                    }
                    produce(Bullet(stats.POS + PointN(25.0*ix, 25.0*iy), PointN(s*ix, s*iy)))
                }
                ready_shoot--
            }

            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.IMPACT) {
                    collapse()
                }
                println(1)
            }
        }
        visuals.add(object: Visualiser() {
            val scale = AnimationScale(0L, 30.0) { x -> 6*(x + 2) }
            val scaleL = AnimationScale(0L, 60.0) { k -> k/2 }

            override fun draw(pos: PointN) {
                agc().fill.color = Color(1.0, 0.0, 0.5, 1.0)
                /*agc().image.draw(Data.get("images/player.png"), stats.POS - game.Game.STEP*36)
                body.forEach { p -> agc().image.draw(Data.get("images/player.png"), p - game.Game.STEP*36) }*/
                agc().fill.rect(
                    pos - Game.STEP*scale.swing(object_time),
                    Game.STEP*2*scale.swing(object_time),
                    Color(0.4, 0.7, 0.15, 1.0))
                agc().fill.rect(
                    pos - PointN(3.0, 2.0)*0.15*scale.swing(object_time),
                    PointN(1.0, 2.0)*0.3*scale.swing(object_time),
                    Color(0.1, 0.1, 0.1, 1.0))
                agc().fill.rect(
                    pos - PointN(-1.0, 2.0)*0.15*scale.swing(object_time),
                    PointN(1.0, 2.0)*0.3*scale.swing(object_time),
                    Color(0.1, 0.1, 0.1, 1.0))
            }

            override fun drawLayer() = DrawLayer(scaleL.swing(object_time))
        })
        bounds.orange = { Bounds(RectN(-Game.STEP*20, Game.STEP*40)) }
        tag("player")
    }

    override fun setValues() {
        name = "cowboy"
        values.add(PosValue(stats.POS))
    }

    override fun interact() = keyboard.pressed(KeyCode.E)
}