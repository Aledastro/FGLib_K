package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject

class MovableWorldController(private var goal: GameObject? = null): WorldController {
    override fun ready(): Boolean {
        if(goal == null) return false
        return !World.active_room.main.into(goal!!.stats.POS)
    }

    override fun action() {
        if(goal == null) return
        val id = World.rooms.indexOfFirst { room -> room.main.into(goal!!.stats.POS) }
        if(id == -1) return
        val rp = World.active_room.pos
        World.set(id)
        if(World.active_room.objects.removeIf { o -> o.tagged("player") })
            World.add(goal!!)
        goal!!.stats.POS -= World.active_room.pos - rp
    }
}
