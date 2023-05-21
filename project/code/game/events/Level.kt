package game.events

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.visual.LayerVisualiser
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.world.World
import com.uzery.fglib.extension.event.CompositeGameEvent
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.num.IntI
import com.uzery.fglib.utils.math.scale.AnimationScale
import game.Game
import game.objects.map.Animation
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

abstract class Level(protected val DURATION_TIME: Int): CompositeGameEvent() {
    //3000*16ms=48s
    protected var ids_time = 0

    init {
        visuals.add(object: LayerVisualiser(DrawLayer(1.0, 3.0)) {
            override fun draw(draw_pos: PointN) {
                agc().fill.oval(
                    draw_pos - stats.POS + PointN(-5, -5), PointN(10, 10), FGUtils.transparent(Color.CORAL, 0.9))
                agc().fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 15.0)
                agc().fill.textC(draw_pos - stats.POS + PointN(0.0, 2.5), "E", Color.WHITE)
            }
        })
        visuals.add(object: Visualiser {
            override fun draw(draw_pos: PointN) {
                val len = Platform.CANVAS.X - 20
                agc().fill.rect(Game.STEP*5, PointN(len, 5.0), Color.BURLYWOOD)
                agc().fill.rect(Game.STEP*5, PointN((ids_time*1.0/DURATION_TIME)*len, 5.0), Color.GREEN)
            }
            override fun drawLayer() = DrawLayer.CAMERA_OFF
        })
        abilities.add(object : AbilityBox{
            override fun run() {
                World.allTagged("player").forEach { it.activate(InputAction(InputAction.CODE.OPEN,"block4",this@Level)) }
            }
        })
        /*bounds.red = {
            Bounds(
                RectN(PointN(-6, 0) - stats.POS, PointN(6, 256)),
                RectN(PointN(256, 0) - stats.POS, PointN(6, 256)),
                RectN(PointN(0, -6) - stats.POS, PointN(256, 6)),
                RectN(PointN(0, 256) - stats.POS, PointN(256, 6)))
        }*/
        tag("level_object")
    }

    override fun finish() {
        produce(object: Animation(
            PointN(128 + 8, 256 - 8),
            "map|arrow.png", IntI(16, 16),
            IntI(0, 0),
            Game.layer("EFF"),
            1,
            2000.0) {

            var start_pos = PointN(stats.POS)

            init {
                abilities.add(object: AbilityBox {
                    private var i_time = 0
                    private val value = AnimationScale(i_time, 40.0) { it - 0.5 }
                    override fun run() {
                        //todo start_pos
                        stats.POS += PointN(0.0, value.cycled(i_time)*2)
                        i_time++
                    }
                })
            }
        })
    }
}