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
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import com.uzery.fglib.utils.math.getter.Drop
import com.uzery.fglib.utils.math.getter.value.PosValue
import com.uzery.fglib.utils.math.num.IntI
import game.Game
import game.objects.character.Bullet
import game.objects.items.EffectItem
import kotlin.math.abs

class CowboyBoss(pos: PointN): Enemy(100) {

    private val SPEED = 0.9

    private enum class MODE(val value: Int) {
        STAY(0), SHOOT(1)
    }

    private var mode = MODE.SHOOT
    private var progress = 0

    init {
        stats.POS = pos
        controller = object: Controller {
            override fun get(): () -> TempAction {
                if(Math.random()<0.4) return attack
                if(Math.random()<0.2) return attack2
                return stay
            }
        }
        abilities.add(object: AbilityBox {
            override fun activate(action: InputAction) {
                if(action.code == InputAction.CODE.DAMAGE) {
                    LIFE -= 2
                }
            }
        })
        val filename = "mob|cowboy_boss.png"
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

            override fun update() {
                goal = World.allTagged("player").firstOrNull()
                mode=MODE.SHOOT
                progress=object_time/10%2
                goal?.let {
                    var r=it.stats.POS.X.compareTo(stats.POS.X)
                    if(abs(it.stats.POS.X-stats.POS.X)<SPEED)r=0
                    stats.nPOS = Game.X*SPEED*r
                }
                if(temp_time%20==0)produce(Bullet(stats.POS+PointN(0,-3)*6, PointN(0,-3)))
            }

            override fun ends() = temp_time>100
        }
    }
    var attack2: () -> TempAction = {
        object: TimeTempAction() {
            val t=90

            override fun update() {
                mode=MODE.SHOOT
                progress=object_time/10%2

                when(temp_time){
                    in 0 until t -> stats.nPOS = Game.X*SPEED
                    in t until t*3 -> stats.nPOS = -Game.X*SPEED
                    in t*5 until t*6 -> stats.nPOS = Game.X*SPEED
                }

                if(temp_time%20==0 && temp_time !in (t*3 until t*6))produce(Bullet(stats.POS+PointN(0,-3)*3, PointN(0,-3)))
            }

            override fun ends() = temp_time>t*6
        }
    }

    var stay: () -> TempAction = {
        object: TimeTempAction() {
            override fun update() {
                mode=MODE.STAY
                progress=object_time/10%2
            }

            override fun ends() = temp_time>20
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

    override fun setValues() {
        name="cowboy_boss"
        values.add(PosValue(stats.POS))
    }
}
