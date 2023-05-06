package game.events

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.extension.event.BaseEvent
import com.uzery.fglib.extension.event.CompositeGameEvent
import com.uzery.fglib.utils.math.geom.PointN
import game.Game
import game.objects.enemy.Goblin
import game.objects.enemy.Ork
import javafx.scene.paint.Color
import java.util.*

class Level_2: CompositeGameEvent() {
    private val DURATION_TIME = 300

    //3000*16ms=48s
    var ids_time = 0

    private val pp: PointN = PointN(16, -48)

    init {
        stats.POS = pp
        visuals.add(object: Visualiser {
            override fun draw(draw_pos: PointN) {
                val len = Platform.CANVAS.X - 20
                agc().fill.rect(Game.STEP*10, PointN(len, 10.0), Color.BURLYWOOD)
                agc().fill.rect(Game.STEP*10, PointN((ids_time*1.0/DURATION_TIME)*len, 10.0), Color.GREEN)
            }

            override fun drawLayer() = DrawLayer.CAMERA_OFF
        })
        add(object: BaseEvent() {
            val wave = LinkedList<Int>()
            override fun start() {
                for(i in 0..DURATION_TIME) {
                    var res = 0
                    if(i%50 == 0) res = 1
                    if(i%500 == 250) res = 5
                    if(i%2000 == 500) res = 25
                    wave.add(res)
                }
            }

            override fun update() {
                for(i in 0 until wave[ids_time]) {
                    val pos = when(Math.random()) {
                        in (0.0..0.25) -> PointN(0, 256)
                        in (0.25..0.5) -> PointN(512, 256)
                        in (0.5..0.75) -> PointN(256, 0)
                        in (0.75..1.0) -> PointN(256, 512)
                        else -> throw IllegalArgumentException()
                    }
                    if(Math.random()<0.2) produce(Ork(pos))
                    else produce(Goblin(pos))
                }
                ids_time++
            }

            override fun ends() = ids_time>=DURATION_TIME
        })
        tag("level_2")
    }

    override fun start() {

    }

    override fun finish() {

    }

    override fun setValues() {
        name = "level_2"
    }
}