package game.events

import com.uzery.fglib.extension.event.BaseEvent
import com.uzery.fglib.utils.math.geom.PointN
import game.objects.enemy.Goblin
import game.objects.enemy.Ork
import java.util.*

class Level_2: Level(3000) {
    init {
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

    override fun setValues() {
        name = "level_2"
    }
}