import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.RunnableU
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color

class Game: RunnableU {
    private var room = Room()

    init {
        room.add(Player(PointN(170.0, 170.0)))
    }

    private var t =0

    override fun update() {
        room.next()
        for(i in 1..10) room.add(ParticleY(PointN(0.0, 0.0), 0.0))
        draw()
        if(t==100) println(room.toString())
        t++
    }

    private fun draw() {
        Platform.graphics.fill.color = Color(1.0, 1.0, 1.0, 1.0)
        Platform.graphics.fill.rect(PointN(0.0, 0.0), PointN(700.0, 700.0))

        room.draw()
    }
}
