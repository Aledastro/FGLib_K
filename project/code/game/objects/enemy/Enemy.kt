package game.objects.enemy

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.AbilityBox
import com.uzery.fglib.utils.math.getter.Drop
import game.objects.character.GameCharacter

abstract class Enemy(life: Int): GameCharacter(life) {
    init {
        tag("enemy")
    }
}
