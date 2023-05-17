package game

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World.Companion.camera
import com.uzery.fglib.core.world.World.Companion.rooms
import com.uzery.fglib.core.world.WorldController
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import game.camera.LazyCamera
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class MovableWC(private var goal: GameObject): WorldController {
    private val void = Room(PointN.ZERO, PointN.ZERO)
    private var goal_room = void

    override fun init() {
        camera = LazyCamera(goal.stats)
        void.add(goal)
    }

    override fun isActive(r: Room): Boolean {
        val p = PointN(200, 200)
        return RectN(r.pos - p, r.size + p*2).into(goal.stats.POS + goal_room.pos)
    }

    private fun isInArea(r: Room): Boolean {
        return RectN(r.pos, r.size).into(goal.stats.POS + goal_room.pos)
    }

    override fun onAppear(r: Room) {
        moveObjs()
    }

    override fun onDisappear(r: Room) {
        for(o in r.objects) {
            /*if(roomFor(o)!=void){
                World.add(o)
            }*/
        }

        moveObjs()
    }

    private fun moveObjs() {
        val room = roomFor(goal)
        goal.stats.POS += goal_room.pos - room.pos
        camera?.move(goal_room.pos - room.pos)

        goal_room.objects.removeIf { o -> o.tagged("player") }
        goal_room = room
        goal_room.objects.removeIf { o -> o.tagged("player") }
        goal_room.objects.add(goal)
    }

    override fun roomFor(o: GameObject): Room {
        return rooms.firstOrNull { isInArea(it) } ?: void
    }

    override fun update() {
        Platform.graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 12.0)
        Platform.graphics.fill.textL(PointN(20, 60), "pos: " + goal.stats.POS, Color.BLACK)
        /*if(Platform.keyboard.pressed(KeyCode.CONTROL) && Platform.keyboard.inPressed(KeyCode.R)) {
            active_rooms.forEach { room->room.objects.removeIf { it.tagged("player") } }
            World.add(goal)
        }*/
        void.next()
        void.draw(PointN.ZERO)

        moveObjs()
    }

    override fun drawPOS(): PointN {
        return goal_room.pos
    }
}
