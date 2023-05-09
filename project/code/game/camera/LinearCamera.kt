package game.camera

import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.core.world.Camera
import com.uzery.fglib.utils.math.geom.PointN

class LinearCamera(private val stats: Stats): Camera {
    override fun drawPOS() = stats.POS
    override fun move(p: PointN) {

    }
}
