package game.camera

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.Camera
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.geom.PointN
import game.Game
import javafx.scene.paint.Color

class LazyCamera(private val stats: Stats): Camera {
    private var pos = PointN(stats.POS)

    private var follow = false

    override fun update() {
        if((pos - stats.POS).length()<10) follow = false
        if((pos - stats.POS).length()>160) follow = true
        if(follow) pos = pos.interpolate(stats.POS, 0.05)
        if(Game.draw_bounds) {
            Platform.graphics.layer = DrawLayer.CAMERA_FOLLOW
            Platform.graphics.stroke.oval(pos + World.active_room.pos - Game.STEP*160, Game.STEP*320, Color.DARKBLUE)
        }

    }

    override fun drawPOS(): PointN {
        return pos - Platform.CANVAS/2
    }

    override fun move(p: PointN) {
        pos -= p
    }
}
