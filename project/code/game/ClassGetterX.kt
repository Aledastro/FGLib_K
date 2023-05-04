package game

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.utils.math.getter.ClassGetterInstance
import game.events.Level_1
import game.events.Level_2
import game.objects.character.Cowboy
import game.objects.enemy.Goblin
import game.objects.enemy.Ork
import game.objects.map.Wall

class ClassGetterX: ClassGetterInstance<GameObject>() {
    override fun addAll() {
        add("level_1") { Level_1() }
        add("level_2") { Level_2() }

        add("cowboy", 1) { Cowboy(pos) }
        add("goblin", 1) { Goblin(pos) }
        add("ork", 1) { Ork(pos) }
        add("wall", 1) { Wall(pos) }
    }
}
