package game

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.getter.ClassGetterInstance
import com.uzery.fglib.utils.math.num.IntI
import game.events.Level_1
import game.events.Level_2
import game.objects.character.Cowboy
import game.objects.enemy.Goblin
import game.objects.enemy.Ork
import game.objects.map.Cactus
import game.objects.map.Decor
import game.objects.map.Wall
import javafx.scene.paint.Color

class ClassGetterX: ClassGetterInstance<GameObject>() {
    override fun addAll() {
        add("level_1") { Level_1() }
        add("level_2") { Level_2() }

        add("cowboy", 1) { Cowboy(pos) }
        add("goblin", 1) { Goblin(pos) }
        add("ork", 1) { Ork(pos) }
        add("wall", 1) { Wall(pos) }
        add("cactus", 1) { Cactus(pos) }

        add("decor", 5) { Decor(pos, string, intI, intI, layer) }

        for(i in 0..10) {
            add("decor#$i") {
                Decor(
                    PointN.ZERO, "map|tiles.png",
                    IntI(16, 16), IntI(i, 2), Game.layer("DRT"))
            }
        }
    }

    private val color256: Color
        get() = if(no_info) Color.BLACK else Color.rgb(int, intX(1), intX(2), intX(3)*1.0/255)

    private val intI: IntI
        get() = if(no_info) IntI(0, 0) else IntI(int, intX(1))
    private val layer: DrawLayer
        get() = if(no_info) DrawLayer.CAMERA_OFF else Game.layer(string)
}
