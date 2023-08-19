package com.uzery.fglib.core.world

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World.rooms
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight

class MovableWC(private val goal: GameObject): WorldController {
    private val void = Room(PointN.ZERO, PointN.ZERO)
    var goal_room = void
        private set

    override fun init() {
    }

    override fun isActive(r: Room): Boolean {
        val p = Platform.CANVAS-PointN(20, 20)
        return RectN(r.pos-p, r.size+p*2).into(goal.stats.POS+goal_room.pos)
    }

    override fun onAppear(r: Room) {
        moveObjs()
    }

    override fun onDisappear(r: Room) {
        /*for (o in r.objects) {
            if(roomFor(o)!=void){
                World.add(o)
            }
        }*/

        moveObjs()
    }

    private fun moveObjs() {
        fun moveGoal() {
            val newRoom = roomFor(goal)
            goal.stats.POS += goal_room.pos-newRoom.pos
            World.camera?.move(goal_room.pos-newRoom.pos)

            goal_room.objects.remove(goal)
            goal_room = newRoom
            goal_room.objects.remove(goal)
            goal_room.objects.add(goal)
        }
        moveGoal()

        fun migrate(oldRoom: Room) {
            oldRoom.objects.forEach { obj ->
                if (obj.tagged("migrator") && !isInArea(oldRoom, obj)) {
                    val newRoom = roomFor(obj)
                    oldRoom.objects.remove(obj)
                    newRoom.objects.add(obj)
                    obj.stats.POS += oldRoom.pos-newRoom.pos
                    obj.stats.roomPOS = newRoom.pos
                    //obj.stats.POS=obj.stats.POS.round(1.0)
                }
            }
        }

        //migrate(void)
        World.active_rooms.forEach { migrate(it) }

        //goal_room.objects.removeIf { o->o.tagged("#immovable") }

        /*if(goal.dead){
            goal=goal_f()
            goal.stats.POS -= goal_room.pos
            camera.move(goal_room.pos - roomFor(goal).pos)
        }*///todo
    }

    private fun isInArea(r: Room, obj: GameObject): Boolean {
        return r.main.into(obj.stats.POS+obj.stats.roomPOS) //+o.stats.roomPOS
    }
    override fun roomFor(obj: GameObject): Room {
        return rooms.firstOrNull { isInArea(it, obj) } ?: void
    }

    override fun update() {
        Platform.graphics.fill.font = Font.font("TimesNewRoman", FontWeight.BOLD, 12.0)
        Platform.graphics.fill.textL(PointN(20, 60), "pos: "+goal.stats.POS, Color.BLACK)
        /*if(Platform.keyboard.pressed(KeyCode.CONTROL) && Platform.keyboard.inPressed(KeyCode.R)) {
            active_rooms.forEach { room->room.objects.removeIf { it.tagged("player") } }
            World.add(goal)
        }*/
        void.next()
        void.draw(PointN.ZERO)

        moveObjs()
    }

    override fun drawPOS(): PointN {
        return goal_room.pos
    }
}
