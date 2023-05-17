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
        if(pos.lengthTo(stats.POS)<5) follow = false
        if(pos.lengthTo(stats.POS)>80) follow = true
        if(follow) pos = pos.interpolate(stats.POS, 0.05)
        if(Platform.develop_mode) {
            Platform.graphics.layer = DrawLayer.CAMERA_FOLLOW
            Platform.graphics.stroke.ovalC(pos, Game.STEP*160, Color.DARKBLUE)
        }
    }

    override fun drawPOS() = pos - Platform.CANVAS/2

    override fun move(p: PointN) {
        pos += p
    }
}
