package game.objects.enemy

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.controller.Controller
import com.uzery.fglib.core.obj.controller.TempAction
import com.uzery.fglib.core.obj.controller.TimeTempAction
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.data.image.Data
import com.uzery.fglib.utils.math.MathUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.Drop
import com.uzery.fglib.utils.math.num.IntI
import game.Game
import game.objects.items.EffectItem
import kotlin.math.cos
import kotlin.math.sin

class Medusa(pos: PointN): Enemy(2) {

    private val SPEED = 0.6

    private enum class MODE(val value: Int) {
        BLOCK(1), ATTACK(0)
    }

    private var mode = MODE.ATTACK
    private var progress = 0

    init {
        stats.POS = pos
        controller = object: Controller {
            override fun get(): () -> TempAction {
                if(Math.random()<0.1) return block
                return attack
            }
        }
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.DAMAGE) {
                    LIFE -= 2
                }
            }
        })
        val filename = "mob|medusa.png"
        Data.set(filename, IntI(16, 16))
        visuals.add(object: LayerVisualiser(Game.layer("OBJ")) {
            override fun draw(draw_pos: PointN) {
                agc().image.drawC(Data.get(filename, IntI(progress, mode.value)), draw_pos)
            }
        })
        bounds.orange = { Bounds(RectN(-Game.STEP*7, Game.STEP*14)) }
    }

    var attack: () -> TempAction = {
        object: TimeTempAction() {
            var goal: GameObject? = null
            override fun start() {
                goal = World.allTagged("player").firstOrNull()
            }

            override fun update() {
                goal?.let {
                    val d = MathUtils.getDegree(stats.POS, it.stats.POS)
                    stats.nPOS += Game.X*cos(d)*SPEED
                    stats.nPOS += Game.Y*sin(d)*SPEED
                }
                progress = object_time/10%2
            }

            override fun ends() = temp_time>20
        }
    }

    var block: () -> TempAction = {
        object: TimeTempAction() {
            override fun start() {
                LIFE += 5
            }

            override fun update() {
                mode = MODE.BLOCK
                progress = (temp_time/10).coerceIn(0..3)
            }

            override fun ends() = false
        }
    }

    override val drop: Drop<GameObject?>
        get() {
            val drop = Drop<GameObject?>()
            drop.setFull(100.0)
            drop.add(2.0) { EffectItem(stats.POS, 3, "coin", 400) }
            drop.add(0.3) { EffectItem(stats.POS, 4, "5-coin", 400) }
            drop.add(1.0) { EffectItem(stats.POS, 5, "wheel_bullets", 400) }
            drop.add(1.0) { EffectItem(stats.POS, 6, "fast_bullets", 400) }
            drop.add(1.0) { EffectItem(stats.POS, 9, "coffee", 400) }
            drop.add(1.0) { EffectItem(stats.POS, 10, "three_bullets", 400) }
            drop.add(1.0) { EffectItem(stats.POS, 11, "life", 400) }
            drop.add(1.0) { EffectItem(stats.POS, 13, "master", 400) }
            return drop
        }
}
