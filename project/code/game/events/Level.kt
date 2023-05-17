package game.events

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.bounds.Bounds
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.extension.event.CompositeGameEvent
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import game.Game
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

abstract class Level(protected val DURATION_TIME: Int): CompositeGameEvent() {
    //3000*16ms=48s
    protected var ids_time = 0
    private val pp: PointN = PointN(16, -48)

    init {
        visuals.add(object: LayerVisualiser(DrawLayer(1.0, 3.0)) {
            override fun draw(draw_pos: PointN) {
                agc().fill.oval(
                    draw_pos - stats.POS + pp + PointN(-10, -10), PointN(20, 20), FGUtils.transparent(Color.CORAL, 0.9))
                agc().fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 15.0)
                agc().fill.textC(draw_pos - stats.POS + pp + PointN(0, 5), "E", Color.WHITE)
            }
        })
        visuals.add(object: Visualiser {
            override fun draw(draw_pos: PointN) {
                val len = Platform.CANVAS.X - 20
                agc().fill.rect(Game.STEP*10, PointN(len, 10.0), Color.BURLYWOOD)
                agc().fill.rect(Game.STEP*10, PointN((ids_time*1.0/DURATION_TIME)*len, 10.0), Color.GREEN)
            }

            override fun drawLayer() = DrawLayer.CAMERA_OFF
        })
        bounds.red = {
            Bounds(
                RectN(PointN(-12, 0) - stats.POS, PointN(12, 512)),
                RectN(PointN(512, 0) - stats.POS, PointN(12, 512)),
                RectN(PointN(0, -12) - stats.POS, PointN(512, 12)),
                RectN(PointN(0, 512) - stats.POS, PointN(512, 12)))
        }
    }

    override fun finish() {

    }
}