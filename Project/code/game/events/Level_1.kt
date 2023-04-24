package game.events

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.extension.event.BaseEvent
import com.uzery.fglib.extension.event.CompositeGameEvent
import com.uzery.fglib.utils.math.geom.PointN
import game.objects.Goblin
import javafx.scene.paint.Color
import java.util.*

class Level_1: CompositeGameEvent() {
    var ids_time = 0

    init {
        visuals.add(object: Visualiser() {
            override fun draw(pos: PointN) {
                agc().fill.rect(pos, PointN(720.0, 10.0), Color.BURLYWOOD)
                agc().fill.rect(pos, PointN((ids_time/6_000.0)*720, 10.0), Color.GREEN)
            }

            override fun drawLayer() = DrawLayer(1.0)
        })
        add(object: BaseEvent() {
            val wave = LinkedList<Int>()
            override fun start() {
                for(i in 0..6_000) {
                    var res = 0
                    if(i%50 == 0) res = 1
                    if(i%500 == 250) res = 10
                    if(i%5000 == 500) res = 100
                    wave.add(res)
                }
            }

            override fun update() {
                for(i in 0 until wave[ids_time]) {
                    when(Math.random()) {
                        in (0.0..0.25) -> produce(Goblin(PointN(20.0, 350.0)))
                        in (0.25..0.5) -> produce(Goblin(PointN(680.0, 350.0)))
                        in (0.5..0.75) -> produce(Goblin(PointN(350.0, 20.0)))
                        in (0.75..1.0) -> produce(Goblin(PointN(350.0, 680.0)))
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
}