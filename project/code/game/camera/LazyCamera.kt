package game.camera

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.Camera
import com.uzery.fglib.utils.math.geom.PointN
import game.Game
import javafx.scene.paint.Color

class LazyCamera(private val stats: Stats): Camera {
    private var pos = PointN(stats.POS)

    private var follow = false

    override fun update() {
        if((pos - stats.POS).length()<10.0) follow = false
        if((pos - stats.POS).length()>160.0) follow = true
        if(follow) pos = pos.interpolate(stats.POS, 0.05)
        if(Game.draw_bounds) {
            Platform.graphics.layer = DrawLayer.CAMERA_FOLLOW
            Platform.graphics.stroke.oval(pos + Game.world.room.pos - Game.STEP*160.0, Game.STEP*320.0, Color.DARKBLUE)
        }

    }

    override fun drawPOS(): PointN {
        return pos - Game.world.room.size*0.5
    }
}
