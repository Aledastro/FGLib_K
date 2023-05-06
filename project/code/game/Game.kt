package game

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import game.objects.character.Cowboy
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

class Game: Extension {
    private var t = 0

    override fun init() {
        World.getter=ClassGetter(ClassGetterX())
        Platform.whole_draw = true

        cowboy=Cowboy(PointN(256,256))

        World.init("project/media/1.room","project/media/2.room")
        World.active_room.objects.removeIf { o->o.tagged("player") }
        World.add(cowboy)
    }

    private var last=0

    private lateinit var cowboy: Cowboy
    override fun update() {
        clear()
        World.next()
        World.draw()

        //world.r().objs().forEach { o -> if((o.stats.POS - STEP*350).length()>500) o.collapse() }

        if(draw_bounds) drawBounds()
        if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds

        if(World.noneExists("level_1","level_2","enemy")){
            World.respawn(last)
            World.active_room.objects.removeIf { o->o.tagged("player") }
            World.add(cowboy)

            last = 2 - last
        }

        Platform.update()
        t++
    }

    private fun clear() {
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.fill.rect(PointN.ZERO, Platform.CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }

    companion object {
        val STEP = PointN(1, 1)

        val X = PointN(1, 0)
        val Y = PointN(0, 1)

        fun randP() = PointN(Math.random(), Math.random())
        fun randP(size: Double) = randP()*size
        fun randP(size: Int) = randP()*size

        var draw_bounds = false
    }


    private fun drawBounds() {
        val dp = World.active_room.pos
        for(o in World.active_room.objects) {
            val c = if(o.stats.fly) Color.color(1.0, 1.0, 0.2, 0.7) else Color.color(1.0, 0.2, 1.0, 0.7)
            graphics.fill.oval(dp + o.stats.POS - STEP*2, STEP*4, c)
        }

        val colors = arrayOf(
            Color.RED,
            Color.ORANGERED,
            Color.BLUE,
            Color.GREEN)

        graphics.setStroke(2.0)
        for(o in World.active_room.objects) {
            for(i in 0 until BoundsBox.SIZE) {
                val bs = o.bounds[i] ?: continue
                for(element in bs().elements) {
                    graphics.fill.draw(dp + o.stats.POS, element.shape, FGUtils.transparent(colors[i],0.1))
                    graphics.stroke.draw(dp + o.stats.POS, element.shape, FGUtils.transparent(colors[i],0.6))
                    graphics.stroke.line(dp + o.stats.POS + element.shape.L, element.shape.S, FGUtils.transparent(colors[i],0.5))
                }
            }
        }
    }
}
