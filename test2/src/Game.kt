import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

class Game: Extension {
    private var draw_bounds = false
    private var world = World(ClassGetterX())

    private var t = 0

    override fun init() {
        world.init("test2/media/1.map")
    }

    override fun update() {
        clear()
        world.run()

        if(Math.random()<0.05) {
            var pos = PointN(Math.random(), Math.random())*660
            pos = PointN((pos.X().toInt()/40)*40.0 + 20, (pos.Y().toInt()/40)*40.0 + 20)
            world.add(Food(pos))
        }

        world.r().objs().forEach { o -> if((o.stats.POS - STEP*350.0).length()>500) o.stats.dead = true }

        if(t == 100) println(world.toString())
        if(draw_bounds) drawBounds()
        if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds

        keyboard.update()
        t++
    }

    private fun clear() {
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.fill.rect(PointN.ZERO, PointN(700.0, 700.0), Color(0.7, 0.6, 0.9, 1.0))
    }

    companion object {
        val STEP = PointN(arrayOf(1.0, 1.0))

        val X = PointN(arrayOf(1.0, 0.0))
        val Y = PointN(arrayOf(0.0, 1.0))
    }


    private fun drawBounds() {
        for(o in world.r().objs()) {
            val c = if(o.stats.fly) Color.color(1.0, 1.0, 0.2, 0.7) else Color.color(1.0, 0.2, 1.0, 0.7)
            graphics.fill.oval(o.stats.POS - STEP*2,  STEP*4, c)
        }

        val colors= arrayOf(Color.color(1.0, 0.0, 0.0),Color.color(1.0, 0.5, 0.0),Color.color(0.0, 0.0, 1.0),Color.color(0.0, 1.0, 0.0))

        for(o in world.r().objs()) {
            for(i in 0 until BoundsBox.size) {
                val bs = o.bounds[i] ?: continue
                for(element in bs().elements) {
                    graphics.fill.draw(o.stats.POS, element.shape, colors[i].interpolate(Color.TRANSPARENT,0.9))
                    graphics.stroke.draw(o.stats.POS, element.shape, colors[i].interpolate(Color.TRANSPARENT,0.5))
                    graphics.stroke.line(o.stats.POS+element.shape.L,o.stats.POS+element.shape.R, colors[i].interpolate(Color.TRANSPARENT,0.5))
                }
            }
        }
    }
}
