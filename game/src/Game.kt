import com.uzery.fglib.core.program.Program
import com.uzery.fglib.core.program.RunnableU
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.paint.Color

class Game: RunnableU {
    private var players = ArrayList<Player>()
    override fun update() {
        players.forEach { p -> p.next() }
        if(Math.random()<0.05) players.add(Player(PointN(0.0, 0.0)))
        draw()
    }

    private fun draw() {
        Program.gc.fill = Color(1.0, 1.0, 1.0, 0.1)
        Program.gc.fillRect(0.0, 0.0, 700.0, 700.0)

        players.forEach { x -> x.draw() }
    }
}
