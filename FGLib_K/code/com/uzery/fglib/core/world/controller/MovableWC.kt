package com.uzery.fglib.core.world.controller

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform.CANVAS
import com.uzery.fglib.core.program.Platform.graphics
import com.uzery.fglib.core.room.Room
import com.uzery.fglib.core.world.World
import com.uzery.fglib.utils.ShapeUtils
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.graphics.data.FGFontWeight
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.shape.RectN

class MovableWC(private val world: World): WorldController {
    private val SIZE = Int.MAX_VALUE/2
    private val SIZE_P = PointN(SIZE, SIZE)

    private val void = Room(-SIZE_P/2, SIZE_P)

    private var goal: GameObject? = null
    private var room_p: Double = 10.0
    fun initZero(goal: GameObject, room_p: Double = 10.0) {
        this.goal = goal
        this.room_p = room_p

        goal.init()
        goal.stats.roomPOS = goal_room.pos
        goal.stats.POS -= void.pos
    }

    val camera_pos
        get() = world.camera?.stats?.realPOS ?: PointN.ZERO

    var goal_room = void
        private set

    override fun init() {

    }

    override fun isActive(r: Room): Boolean {
        val pos = PointN(room_p, -room_p)
        val rect1 = RectN(r.pos-pos, r.size+pos*2)
        val rect2 = RectN(r.pos+pos, r.size-pos*2)
        val camera_main = RectN.C(camera_pos, CANVAS)
        return ShapeUtils.into(rect1, camera_main) || ShapeUtils.into(rect2, camera_main)
    }

    override fun onAppear(r: Room) {
        moveObjs()
    }

    override fun onDisappear(r: Room) {
        /*for (o in r.objects) {
            if(roomFor(o)!=void){
                world.add(o)
            }
        }*/

        moveObjs()
    }

    private fun moveObjs() {
        fun moveGoal() {
            val goal = goal?: return

            val newRoom = roomFor(goal)
            goal.stats.POS += goal_room.pos-newRoom.pos
            goal.stats.roomPOS = newRoom.pos
            world.camera?.stats?.roomPOS = goal.stats.roomPOS
            world.camera?.move(goal_room.pos-newRoom.pos)

            goal_room.objects.remove(goal)
            goal_room = newRoom
            goal_room.objects.add(goal)
        }
        moveGoal()

        fun migrate(oldRoom: Room) {
            val objectsToRemove = ArrayList<GameObject>()
            val objectsToAdd = ArrayList<Pair<Room, GameObject>>()
            oldRoom.objects.filter { it.tagged("migrator") }.forEach { obj ->
                if (!isInArea(oldRoom, obj) || oldRoom == void) {
                    val newRoom = roomFor(obj)
                    objectsToRemove.add(obj)
                    objectsToAdd.add(Pair(newRoom, obj))
                    obj.stats.POS += oldRoom.pos-newRoom.pos
                    obj.stats.roomPOS = newRoom.pos
                }
            }
            oldRoom.objects.removeAll(objectsToRemove.toSet())
            objectsToAdd.forEach { it.first.objects.add(it.second) }
        }

        migrate(void)
        world.active_rooms.forEach { migrate(it) }
    }

    private fun isInArea(r: Room, obj: GameObject): Boolean {
        return r.main.into(obj.stats.realPOS)
    }

    override fun roomFor(obj: GameObject): Room {
        return world.rooms.firstOrNull { isInArea(it, obj) } ?: void
    }

    override fun update() {
        graphics.fill.font("TimesNewRoman", 12.0/2, FGFontWeight.BOLD)
        graphics.fill.textL(PointN(20, 60), "pos: "+goal?.stats?.POS, FGColor.BLACK)
        /*if(Platform.keyboard.pressed(KeyCode.CONTROL) && Platform.keyboard.inPressed(KeyCode.R)) {
            active_rooms.forEach { room->room.objects.removeIf { it.tagged("player") } }
            world.add(goal)
        }*/
        void.next()

        moveObjs()
    }

    override fun drawPOS(): PointN {
        return goal_room.pos
    }

    override fun draw() {
        void.draw(PointN.ZERO)
    }
}
