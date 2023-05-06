package game.objects.enemy

import game.objects.character.GameCharacter

abstract class Enemy(life: Int): GameCharacter(life) {
    init {
        tag("enemy")
    }
}
