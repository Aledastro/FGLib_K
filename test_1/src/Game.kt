import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.program.Platform.Companion.mouse_keys
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.geom.PointN
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Game: Extension {
    private var draw_bounds = false
    private var world = World(ClassGetterX())

    private var t = 0

    override fun init() {
        world.init("test_1/media/1.map")
    }

    override fun update() {
        clear()
        world.run()

        for(i in 1..15) {
            val alpha = Math.random()*2*PI
            val pos = STEP*350 + PointN(cos(alpha), sin(alpha))*300
            world.add(ParticleY(pos, 0.0))
            //world.add(ParticleY(STEP*350, 0.0))
        }

        world.r().objs().forEach { o -> if((o.stats.POS - STEP*350.0).length()>500) o.collapse() }

        if(t == 100) println(world.toString())
        if(draw_bounds) drawBounds()
        if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds

        keyboard.update()
        mouse_keys.update()
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

        fun randP() = PointN(Math.random(), Math.random())
        fun randP(size: Double) = randP()*size
        fun randP(size: Int) = randP()*size
    }


    private fun drawBounds() {
        graphics.setStroke(2.0)
        for(o in world.r().objs()) {
            val c = if(o.stats.fly) Color.color(1.0, 1.0, 0.2, 0.7) else Color.color(1.0, 0.2, 1.0, 0.7)
            graphics.fill.oval(o.stats.POS - PointN(2.0, 2.0), PointN(4.0, 4.0), c)
        }
        for(o in world.r().objs()) {
            val bs = o.bounds.orange ?: continue
            for(element in bs().elements) {
                graphics.fill.draw(o.stats.POS, element.shape, Color.color(0.7, 0.2, 0.1, 0.1))
                graphics.stroke.draw(o.stats.POS, element.shape, Color.color(0.7, 0.2, 0.1, 0.6))
            }
        }
        for(o in world.r().objs()) {
            val bs = o.bounds.red ?: continue
            for(element in bs().elements) {
                graphics.fill.draw(o.stats.POS, element.shape, Color.color(0.7, 0.1, 0.1, 0.1))
                graphics.stroke.draw(o.stats.POS, element.shape, Color.color(0.7, 0.1, 0.1, 0.6))
            }
        }
        for(o in world.r().objs()) {
            val bs = o.bounds.green ?: continue
            for(element in bs().elements) {
                graphics.fill.draw(o.stats.POS, element.shape, Color.color(0.1, 0.7, 0.1, 0.1))
                graphics.stroke.draw(o.stats.POS, element.shape, Color.color(0.1, 0.7, 0.1, 0.6))
            }
        }
        for(o in world.r().objs()) {
            val bs = o.bounds.blue ?: continue
            for(element in bs().elements) {
                graphics.fill.draw(o.stats.POS, element.shape, Color.color(0.1, 0.1, 0.7, 0.1))
                graphics.stroke.draw(o.stats.POS, element.shape, Color.color(0.1, 0.1, 0.7, 0.6))
            }
        }
    }
}
