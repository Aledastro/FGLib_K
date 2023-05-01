package game

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.getter.ClassGetterInstance
import game.events.Level_1
import game.objects.character.Cowboy
import game.objects.map.Wall

class ClassGetterX: ClassGetterInstance<GameObject>() {
    override fun addAll() {
        add("level_1", 0) { Level_1() }
        add("cowboy", 1) { Cowboy(pos) }
        add("wall", 1) { Wall(pos) }
    }
}
