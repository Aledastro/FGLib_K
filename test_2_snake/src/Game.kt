import com.uzery.fglib.core.obj.bounds.BoundsBox
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
        world.init("test_2_snake/media/1.map")
        for(i in 1..16) {
            world.add(Wall(PointN(i*40+20.0,20.0)))
            world.add(Wall(PointN(i*40+20.0,700.0)))
            world.add(Wall(PointN(20.0,i*40+20.0)))
            world.add(Wall(PointN(700.0,i*40+20.0)))
        }
    }

    override fun update() {
        clear()
        world.run()

        if(Math.random()<0.005) {
            val pos = STEP*60+randP(640).round(40.0)
            world.add(Food(pos))
        }
        if(Math.random()<0.0005) {
            val pos = STEP*60+randP(640).round(40.0)
            world.add(SuperFood(pos))
        }
        /*for(i in 1..15) {
            val alpha = Math.random()*2*PI
            val pos = STEP*350 + PointN(cos(alpha), sin(alpha))*300
            //world.add(ParticleY(pos, 0.0))
            world.add(ParticleY(STEP*350, 0.0))
        }*/

        world.r().objs().forEach { o -> if((o.stats.POS - STEP*350).length()>500) o.stats.dead = true }

        if(t == 100) println(world.toString())
        if(draw_bounds) drawBounds()
        if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds

        keyboard.update()
        mouse_keys.update()
        t++
    }

    private fun clear() {
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.fill.rect(PointN.ZERO, STEP*720, Color(0.7, 0.6, 0.9, 1.0))
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
        for(o in world.r().objs()) {
            val c = if(o.stats.fly) Color.color(1.0, 1.0, 0.2, 0.7) else Color.color(1.0, 0.2, 1.0, 0.7)
            graphics.fill.oval(o.stats.POS - STEP*2, STEP*4, c)
        }

        val colors = arrayOf(
            Color.RED,
            Color.ORANGERED,
            Color.BLUE,
            Color.GREEN)//todo why?

        graphics.setStroke(2.0)
        for(o in world.r().objs()) {
            for(i in 0 until BoundsBox.size) {
                val bs = o.bounds[i] ?: continue
                for(element in bs().elements) {
                    graphics.fill.draw(o.stats.POS, element.shape, colors[i].interpolate(Color.TRANSPARENT, 0.9))
                    graphics.stroke.draw(o.stats.POS, element.shape, colors[i].interpolate(Color.TRANSPARENT, 0.4))
                    graphics.stroke.line(
                        o.stats.POS + element.shape.L,
                        o.stats.POS + element.shape.R,
                        colors[i].interpolate(Color.TRANSPARENT, 0.5))
                }
            }
        }
    }
}
