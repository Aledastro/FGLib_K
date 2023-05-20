package game.objects.character

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.controller.TimeTempAction
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.utils.data.image.Data
import com.uzery.fglib.utils.math.geom.Direct
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.Drop
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.num.IntI
import game.Game
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

class Cowboy(pos: PointN): GameCharacter(1000) {

    init {
        stats.POS = pos
        controller = object: Controller {
            override fun get(): () -> TempAction {
                if(readyShoot()) return shoot
                if(readyMove()) return move
                return stay
            }
        }

        abilities.add(object: AbilityBox {
            override fun run() {
                LIFE++
                LIFE = LIFE.coerceIn(0..1000)
            }

            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.IMPACT) {
                    LIFE -= 10
                }
            }
        })
        val filename = "char|cowboy.png"
        Data.set(filename, IntI(16, 16))
        visuals.add(object: LayerVisualiser(Game.layer("OBJ")) {
            override fun draw(draw_pos: PointN) {
                when(mode) {
                    MODE.STAY -> agc().image.drawC(Data.get(filename, IntI(1, 1)), draw_pos)
                    MODE.MOVE, MODE.SHOOT -> {
                        agc().image.drawC(
                            Data.get(filename, IntI(drawDirect(direct).ordinal, 0)),
                            draw_pos)
                        val ii = IntI(if(readyMove()) object_time/7%4 else 1, 2)
                        agc().image.drawC(Data.get(filename, ii), draw_pos)
                    }
                }
            }

            private fun drawDirect(direct: Direct): Direct {
                return when(direct) {
                    Direct.DOWN_LEFT -> Direct.LEFT
                    Direct.DOWN_RIGHT -> Direct.RIGHT
                    Direct.UP_LEFT -> Direct.LEFT
                    Direct.UP_RIGHT -> Direct.RIGHT
                    Direct.CENTER -> Direct.DOWN
                    else -> direct
                }
            }
        })
        visuals.add(object: LayerVisualiser(DrawLayer(0.0, 3.0)) {
            override fun draw(draw_pos: PointN) {
                agc().fill.rect(
                    PointN.ZERO,
                    Platform.CANVAS,
                    Color.color(1.0, 0.0, 0.0, (1.0 - LIFE/1000.0).coerceIn(0.0, 0.1)))
            }
        })

        bounds.orange = { Bounds(RectN(PointN(-6, -3), Game.STEP*12)) }
        tag("player")
    }

    override val drop = Drop<GameObject?>()

    override fun setValues() {
        name = "cowboy"
        values.add(PosValue(stats.POS))
    }

    override fun interact() = keyboard.pressed(KeyCode.E)

    var mode = MODE.STAY
    var direct = Direct.DOWN

    enum class MODE { STAY, MOVE, SHOOT }

    val cowboy_speed
        get() = if(effectedAny("coffee", "master")) 2.4 else 1.4

    val shoot: () -> TempAction = {
        object: TimeTempAction() {
            override fun start() {
                val s = 4.0
                direct = Direct.CENTER
                when {
                    keyboard.pressed(KeyCode.UP) -> direct += Direct.UP
                    keyboard.pressed(KeyCode.DOWN) -> direct += Direct.DOWN
                }
                when {
                    keyboard.pressed(KeyCode.LEFT) -> direct += Direct.LEFT
                    keyboard.pressed(KeyCode.RIGHT) -> direct += Direct.RIGHT
                }
                shootTo(stats.POS, direct.p*s)
            }

            private fun shootTo(pos: PointN, speed: PointN) {
                val xc = 4
                when {
                    effected("wheel_bullets") && effectedAny("three_bullets", "master") -> {
                        for(i in 0..7) {
                            for(j in -1..1) {
                                val sp = speed.rotateXY(i*Math.PI/4 + j*Math.PI/8)
                                produce(Bullet(pos + sp*xc, sp))
                            }
                        }
                    }

                    effectedAny("three_bullets", "master") -> {
                        for(i in -1..1) {
                            val sp = speed.rotateXY(i*Math.PI/8)
                            produce(Bullet(pos + sp*xc, sp))
                        }
                    }

                    effected("wheel_bullets") -> {
                        for(i in 0..7) {
                            val sp = speed.rotateXY(i*Math.PI/4)
                            produce(Bullet(pos + sp*xc, sp))
                        }
                    }

                    else -> produce(Bullet(pos + speed*xc, speed))
                }
            }

            override fun update() {
                mode = MODE.SHOOT
                var dir = Direct.CENTER
                if(keyboard.pressed(KeyCode.W)) dir += Direct.UP
                if(keyboard.pressed(KeyCode.S)) dir += Direct.DOWN
                if(keyboard.pressed(KeyCode.A)) dir += Direct.LEFT
                if(keyboard.pressed(KeyCode.D)) dir += Direct.RIGHT
                stats.nPOS += dir.p*cowboy_speed
            }

            override fun ends() = temp_time>if(effectedAny("fast_bullets", "master")) 2 else 8
        }
    }
    val move: () -> TempAction = {
        object: TimeTempAction() {
            override fun update() {
                mode = MODE.MOVE

                direct = Direct.CENTER
                if(keyboard.pressed(KeyCode.W)) direct += Direct.UP
                if(keyboard.pressed(KeyCode.S)) direct += Direct.DOWN
                if(keyboard.pressed(KeyCode.A)) direct += Direct.LEFT
                if(keyboard.pressed(KeyCode.D)) direct += Direct.RIGHT

                stats.nPOS += direct.p*cowboy_speed
            }

            override fun ends() = temp_time>20 || readyShoot() || !readyMove()
        }
    }
    val stay: () -> TempAction = {
        object: TimeTempAction() {
            override fun update() {
                mode = MODE.STAY
            }

            override fun ends() = temp_time>20 || readyMove() || readyShoot()
        }
    }

    private fun readyMove() = keyboard.anyPressed(KeyCode.W, KeyCode.S, KeyCode.D, KeyCode.A)
    private fun readyShoot() = keyboard.anyPressed(KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT)
}