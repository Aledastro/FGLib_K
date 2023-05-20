package game

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.program.Extension
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.program.Platform.Companion.develop_mode
import com.uzery.fglib.core.program.Platform.Companion.graphics
import com.uzery.fglib.core.program.Platform.Companion.keyboard
import com.uzery.fglib.core.world.World
import com.uzery.fglib.core.world.WorldUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetter
import com.uzery.fglib.utils.math.num.StringD
import game.objects.character.Cowboy
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color

class Game: Extension {
    private var t = 0

    override fun init() {
        World.getter = ClassGetter(ClassGetterX())
        Platform.whole_draw = true

        cowboy = Cowboy(PointN(256, 128))

        World.init(MovableWC(cowboy), "files/map/1.room", "files/map/2.room", "files/map/3.room", "files/map/4.room", "files/map/5.room")
        //World.init(MovableWC(cowboy), "1.room", "3.room", "5.room")
        /*World.active_rooms.forEach { room -> room.objects.removeIf { o -> o.tagged("player") } }
        World.add(cowboy)*/
    }

    private var last = 0


    private var draw_bounds = false

    private lateinit var cowboy: Cowboy

    override fun update() {
        clear()
        World.next()
        World.draw()
        graphics.layer = DrawLayer.CAMERA_FOLLOW
        val c = Color.gray(0.04)
        val sx = 512
        val sy = 512
        graphics.fill.rect(ppp + PointN(-sx, -sy), PointN(sx*2 + 256, sy), c)
        graphics.fill.rect(ppp + PointN(-sx, 256), PointN(sx*2 + 256, sy), c)
        graphics.fill.rect(ppp + PointN(-sx, -sy), PointN(sx, sy*2 + 256), c)
        graphics.fill.rect(ppp + PointN(256, -sy), PointN(sx, sy*2 + 256), c)

        if(keyboard.pressed(KeyCode.CONTROL) && keyboard.inPressed(KeyCode.TAB)) draw_bounds = !draw_bounds
        if(draw_bounds) World.active_rooms.forEach { room -> WorldUtils.drawBounds(room, room.pos) }

        develop_mode = draw_bounds

        //world.r().objs().forEach { o -> if((o.stats.POS - STEP*350).length()>500) o.collapse() }

        /*if(World.noneExists("level_1", "level_2", "enemy")) {
            World.respawn(last)
            World.active_room.objects.removeIf { o -> o.tagged("player") }
            World.add(cowboy)

            last = 1 - last
        }*/

        Platform.update()
        t++
    }

    private fun clear() {
        graphics.fill.color = Color(0.7, 0.6, 0.9, 1.0)
        graphics.layer = DrawLayer.CAMERA_OFF
        graphics.fill.rect(PointN.ZERO, Platform.CANVAS, Color(0.7, 0.6, 0.9, 1.0))
    }

    companion object {
        //todo remove ppp
        var ppp = PointN.ZERO
        const val current_filename = "files/map/4.room"

        private val layers = HashMap<String, DrawLayer>()

        init {
            //todo BG & SP
            val names = arrayOf(
                StringD("SKY", 0.0),
                StringD("BG1", 0.2),
                StringD("BG2", 0.4),
                StringD("BG3", 0.8),
                StringD("DRT", 1.0),
                StringD("OBJ", 1.0),
                StringD("BLK", 1.0),
                StringD("EFF", 1.0),
                StringD("SP1", 1.2),
                StringD("SP2", 1.5),
                StringD("SP3", 2.0),
                StringD("UI", 0.0))
            val suf = arrayOf("--", "-", "", "+", "++")
            fun addL(name: String, z: Double, sort: Int) {
                for(i in 0..2) {
                    layers[name + suf[i]] = DrawLayer(z, sort*10 + i, name + suf[i])
                }
            }
            for(i in names.indices) {
                addL(names[i].s, names[i].d, i)
            }
        }

        val STEP = PointN(1, 1)

        val X = PointN(1, 0)
        val Y = PointN(0, 1)

        fun randP() = PointN(Math.random(), Math.random())
        fun randP(size: Double) = randP()*size
        fun randP(size: Int) = randP()*size
        fun layer(input: String): DrawLayer {
            return layers[input] ?: throw DebugData.error("from: $input")
        }
    }
}
