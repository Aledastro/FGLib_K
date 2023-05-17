package game.camera

import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.Camera
import com.uzery.fglib.utils.math.geom.PointN
import game.Game

class LevelCamera: Camera {
    private var pos = PointN.ZERO

    private var stops = listOf(
        PointN(0, 0),
        PointN(0, 256),
        PointN(0, 512),
        PointN(0, 768),
        PointN(0, 1024),
        PointN(0, 1280))

    var next = 0

    private var f_pos = pos
    private var d_pos = PointN.ZERO
    override fun update() {
        if(pos.lengthTo(f_pos)>10) {
            pos += d_pos
        }
    }

    override fun drawPOS(): PointN {
        return pos - Platform.CANVAS/2 + PointN(128, 128) //todo
    }

    override fun move(p: PointN) {
        pos += p
        d_pos = (f_pos - pos)/f_pos.lengthTo(pos)*10
        Game.ppp -= p
    }
}
