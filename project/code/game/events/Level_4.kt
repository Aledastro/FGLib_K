package game.events

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.extension.event.BaseEvent
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.Drop
import game.objects.enemy.Goblin
import game.objects.enemy.Medusa
import game.objects.enemy.Ork
import java.util.*

class Level_4: Level(3000) {
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
                    val obj = Drop<GameObject>()

                    fun getP(): PointN {
                        val pos = Drop<PointN>()
                        pos.add { PointN(0, 128) }
                        pos.add { PointN(256, 128) }
                        pos.add { PointN(128, 0) }
                        pos.add { PointN(128, 256) }
                        return pos.get2()
                    }

                    obj.add(0.0) { Goblin(getP()) }
                    obj.add(0.0) { Ork(getP()) }
                    obj.add(1.0) { Medusa(getP()) }

                    produce(obj.get2())
                }
                ids_time++
            }

            override fun ends() = ids_time>=DURATION_TIME
        })
        tag("level_4")
    }

    override fun start() {

    }

    override fun setValues() {
        name = "level_4"
    }
}