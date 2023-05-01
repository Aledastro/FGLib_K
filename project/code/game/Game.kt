package game

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import game.objects.map.Wall
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

class Game: Extension {
    private var t = 0

    override fun init() {
        world.init("project/media/1.room")
        for(i in 0..17) {
            if(i in 7..9) continue
            world.add(Wall(PointN(i*40 + 20.0, 20.0)))
            world.add(Wall(PointN(i*40 + 20.0, 700.0)))
            world.add(Wall(PointN(20.0, i*40 + 20.0)))
            world.add(Wall(PointN(700.0, i*40 + 20.0)))
        }
        for(i in 6..10) {
            for(j in 6..10) {
                if(i in 7..9 && j in 7..9) continue
                world.add(Wall(PointN(i*40 + 20.0, j*40 + 20.0)))
            }
        }
        Platform.whole_draw = true
    }

    override fun update() {
        clear()
        World.next()
        World.draw()

        //world.r().objs().forEach { o -> if((o.stats.POS - STEP*350).length()>500) o.collapse() }

        if(draw_bounds) drawBounds()
        if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds

        Platform.update()
        t++
    }

    private fun clear() {
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.fill.rect(PointN.ZERO, Platform.CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }

    var world = World(ClassGetter(ClassGetterX()))

    companion object {
        val STEP = PointN(1.0, 1.0)

        val X = PointN(1.0, 0.0)
        val Y = PointN(0.0, 1.0)

        fun randP() = PointN(Math.random(), Math.random())
        fun randP(size: Double) = randP()*size
        fun randP(size: Int) = randP()*size

        var draw_bounds = false
    }


    private fun drawBounds() {
        val dp = World.room.pos
        for(o in World.room.objects) {
            val c = if(o.stats.fly) Color.color(1.0, 1.0, 0.2, 0.7) else Color.color(1.0, 0.2, 1.0, 0.7)
            graphics.fill.oval(dp + o.stats.POS - STEP*2, STEP*4, c)
        }

        val colors = arrayOf(
            Color.RED,
            Color.ORANGERED,
            Color.BLUE,
            Color.GREEN)

        graphics.setStroke(2.0)
        for(o in World.room.objects) {
            for(i in 0 until BoundsBox.size) {
                val bs = o.bounds[i] ?: continue
                for(element in bs().elements) {
                    graphics.fill.draw(dp + o.stats.POS, element.shape, colors[i].interpolate(Color.TRANSPARENT, 0.9))
                    graphics.stroke.draw(dp + o.stats.POS, element.shape, colors[i].interpolate(Color.TRANSPARENT, 0.4))
                    graphics.stroke.line(
                        dp + o.stats.POS + element.shape.L,
                        dp + o.stats.POS + element.shape.R,
                        colors[i].interpolate(Color.TRANSPARENT, 0.5))
                }
            }
        }
    }
}
