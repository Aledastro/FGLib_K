import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.RunnableU
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color

class Game: RunnableU {
    private var world = World(ClassGetterX())

    private var t = 0

    override fun init(){
        world.init("game/media/1.map")
    }

    override fun update() {
        clear()
        world.run()

        for(i in 1..10) world.add(ParticleY(PointN.ZERO, 0.0))

        if(t == 100) println(world.toString())
        t++
    }

    private fun clear() {
        Platform.graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        Platform.graphics.fill.rect(PointN.ZERO, PointN(700.0, 700.0))
    }

    companion object {
        val STEP = PointN(arrayOf(1.0,1.0))

        val X = PointN(arrayOf(1.0,0.0))
        val Y = PointN(arrayOf(0.0,1.0))
        val Z = PointN(arrayOf(0.0,0.0))
    }
}
