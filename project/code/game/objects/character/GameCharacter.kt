package game.objects.character

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.utils.math.getter.Drop

abstract class GameCharacter(protected var LIFE: Int): GameObject() {

    abstract val drop: Drop<GameObject?>

    init {
        abilities.add(object: AbilityBox {
            override fun run() {
                if(LIFE<=0) {
                    drop.get()?.also { produce(it) }
                    collapse()
                }
            }
        })
    }
}
