package game

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.World.Companion.active_room
import com.uzery.fglib.core.world.World.Companion.camera
import com.uzery.fglib.core.world.WorldController
import com.uzery.fglib.utils.math.MathUtils
import game.camera.LazyCamera
import javafx.scene.input.KeyCode

class MovableWorldController(private var goal: GameObject): WorldController {

    override fun init() {
        camera = LazyCamera(goal.stats)
        println(MathUtils.round(-21.3, 1.0))
    }

    override fun ready(): Boolean {
        return !active_room.main.into(active_room.pos + goal.stats.POS)
    }

    override fun changeRoom() {
        val id = World.rooms.indexOfFirst { room -> room.main.into(active_room.pos + goal.stats.POS) }
        if(id == -1) return
        val rp = active_room.pos
        World.set(id)
        if(active_room.objects.removeIf { o -> o.tagged("player") }) {
            World.add(goal)
        }

        //512 512 -> 0 512
        goal.stats.POS -= active_room.pos - rp
        camera?.move(active_room.pos - rp)
    }

    override fun update() {
        if(Platform.keyboard.pressed(KeyCode.CONTROL) && Platform.keyboard.inPressed(KeyCode.R)) {
            World.reset()
            if(active_room.objects.removeIf { o -> o.tagged("player") }) {
                World.add(goal)
            }
        }
    }
}
