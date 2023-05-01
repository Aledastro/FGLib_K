package game.events

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.extension.event.BaseEvent
import com.uzery.fglib.extension.event.CompositeGameEvent
import com.uzery.fglib.utils.math.geom.PointN
import game.Game
import game.objects.enemy.Goblin
import javafx.scene.paint.Color
import java.util.*

class Level_1: CompositeGameEvent() {
    var ids_time = 0

    private val pp: PointN = PointN(16.0, -48.0)

    init {
        stats.POS = pp
        visuals.add(object: Visualiser {
            override fun draw(draw_pos: PointN) {
                val s = Platform.CANVAS.X - 20
                agc().fill.rect(Game.STEP*10, PointN(s, 10.0), Color.BURLYWOOD)
                agc().fill.rect(Game.STEP*10, PointN((ids_time/6_000.0)*s, 10.0), Color.GREEN)
            }

            override fun drawLayer() = DrawLayer.CAMERA_OFF
        })
        add(object: BaseEvent() {
            val wave = LinkedList<Int>()
            override fun start() {
                for(i in 0..6_000) {
                    var res = 0
                    if(i%50 == 0) res = 1
                    if(i%500 == 250) res = 5
                    if(i%5000 == 500) res = 25
                    wave.add(res)
                }
            }

            override fun update() {
                for(i in 0 until wave[ids_time]) {
                    when(Math.random()) {
                        in (0.0..0.25) -> produce(Goblin(PointN(0.0, 256.0)))
                        in (0.25..0.5) -> produce(Goblin(PointN(512.0, 256.0)))
                        in (0.5..0.75) -> produce(Goblin(PointN(256.0, 0.0)))
                        in (0.75..1.0) -> produce(Goblin(PointN(256.0, 512.0)))
                    }
                }
                ids_time++
            }

            override fun ends() = ids_time>=6_000
        })
    }

    override fun start() {

    }

    override fun finish() {

    }

    override fun setValues() {
        name = "level_1"
    }
}