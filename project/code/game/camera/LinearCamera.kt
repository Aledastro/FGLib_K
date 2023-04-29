package game.camera

import com.uzery.fglib.core.obj.stats.Stats
import com.uzery.fglib.core.world.Camera

class LinearCamera(private val stats: Stats): Camera {
    override fun drawPOS() = stats.POS
}
