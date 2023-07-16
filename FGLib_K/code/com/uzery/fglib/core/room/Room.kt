package com.uzery.fglib.core.room

import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.obj.ability.InputAction
import com.uzery.fglib.core.obj.bounds.BoundsBox
import com.uzery.fglib.core.obj.bounds.BoundsElement
import com.uzery.fglib.core.obj.visual.Visualiser
import com.uzery.fglib.utils.data.file.ConstL
import com.uzery.fglib.utils.data.getter.value.PosValue
import com.uzery.fglib.utils.data.getter.value.SizeValue
import com.uzery.fglib.utils.math.BoundsUtils
import com.uzery.fglib.utils.math.FGUtils
import com.uzery.fglib.utils.math.ShapeUtils
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.RectN
import java.util.*
import kotlin.math.sign

class Room(val pos: PointN, val size: PointN) {
    //todo private
    val objects = LinkedList<GameObject>()
    private val new_objects = ArrayList<GameObject>()
    private val old_objects = HashSet<GameObject>()

    val main = RectN(pos, size)

    constructor(pos: PointN, size: PointN, objs: List<GameObject>): this(pos, size) {
        objects.addAll(objs)
    }

    var t = 0
    fun next() {
        objects.addAll(new_objects)
        new_objects.clear()

        objects.forEach { it.next() }

        updateCells()
        t++
        nextMove()
        //nextActivate()

        objects.forEach { new_objects.addAll(it.children) }
        objects.forEach { it.children.clear() }

        objects.removeIf { it.dead || it.owner != null }

        objects.removeAll(old_objects)
        old_objects.clear()
    }

    fun draw(draw_pos: PointN) {
        val vis = ArrayList<Visualiser>()
        val pos_map = HashMap<Visualiser, PointN>()
        objects.forEach { obj ->
            vis.addAll(obj.visuals)
            obj.visuals.forEach { pos_map[it] = obj.stats.POS }
        }
        drawVisuals(draw_pos, vis, pos_map)
    }

    companion object {
        fun drawVisuals(draw_pos: PointN, vis: ArrayList<Visualiser>, pos_map: HashMap<Visualiser, PointN>) {
            vis.sortWith { v1, v2 ->
                when {
                    v1.drawLayer().sort != v2.drawLayer().sort -> (v1.drawLayer().sort - v2.drawLayer().sort).toInt()
                    else -> sign(pos_map[v1]!!.Y - pos_map[v2]!!.Y).toInt()
                }
            }
            vis.forEach { visual ->
                visual.agc.layer = visual.drawLayer()
                visual.drawWithDefaults(draw_pos + pos_map[visual]!!)
            }
        }
    }


    //todo remove from objects not new_objects
    fun add(vararg objs: GameObject) = new_objects.addAll(objs)
    fun remove(vararg objs: GameObject) = old_objects.addAll(objs)

    fun add(objs: List<GameObject>) = new_objects.addAll(objs)
    fun remove(objs: List<GameObject>) = old_objects.addAll(objs)

    private val ROOM_GRID = 64 //todo to project files

    private val dim = 2 // todo objects.first.stats.POS.dimension()
    private val cell_sizes = Array(dim) { -1..size[it].toInt()/ROOM_GRID + 1 }
    private val cells_size = BoundsBox.SIZE*(cell_sizes[0].last + 2)*(cell_sizes[1].last + 2)

    private val cells = HashMap<CellData, LinkedList<Int>>()
    private val cells_ranges = LinkedList<LinkedList<IntRange>>()

    private fun updateCells() {
        val t = System.nanoTime()

        cells.clear()
        cells_ranges.clear()

        for(i in 0 until dim) {
            cells_ranges.add(LinkedList())

            for(j in 0 until objects.size) {
                cells_ranges[i].add(0..0)
            }
        }

        for(id in objects.indices) {
            updateCellsFor(id, BoundsBox.RED)
            updateCellsFor(id, BoundsBox.ORANGE)
            updateCellsFor(id, BoundsBox.BLUE)
            updateCellsFor(id, BoundsBox.GREEN)
        }
        println(System.nanoTime() - t)
    }

    private fun updateCellsFor(id: Int, bounds_color: Int) {
        val obj = objects[id]
        val main = obj.bounds[bounds_color].main() ?: return

        val move_area = ShapeUtils.rectX(main.copy(obj.stats.POS), main.copy(obj.stats.POS + obj.stats.nPOS))

        val x1 = (move_area.L[0].toInt()/ROOM_GRID).coerceIn(cell_sizes[0])
        val x2 = (move_area.R[0].toInt()/ROOM_GRID).coerceIn(cell_sizes[0])
        val y1 = (move_area.L[1].toInt()/ROOM_GRID).coerceIn(cell_sizes[1])
        val y2 = (move_area.R[1].toInt()/ROOM_GRID).coerceIn(cell_sizes[1])

        //todo n dim

        for(ix in x1..x2) {
            for(iy in y1..y2) {
                val data = CellData(ix, iy, bounds_color)
                if(!cells.contains(data)) cells[data] = LinkedList()
                cells[data]!!.add(id)
            }
        }
        cells_ranges[0][id] = x1..x2 //cell_sizes[0]
        cells_ranges[1][id] = y1..y2 //cell_sizes[1]

        //println("O/C: "+objects.size + " - "+cells.sumOf { it.count() }+" "+sum)
    }

    private fun nextMove() {
        for(id in objects.indices) {
            val obj = objects[id]
            obj.stats.lPOS = obj.stats.POS
            if(obj.tagged("#immovable")) continue

            val move_bs = obj.bounds.orange
            if(move_bs.isEmpty()) continue

            fun maxMove(move_p: PointN): Double {
                val rangeX = cells_ranges[0][id]
                val rangeY = cells_ranges[1][id]

                fun maxPathIn(ix: Int, iy: Int): Double {
                    val cell = cells[CellData(ix, iy, BoundsBox.RED)] ?: return 1.0

                    return cell.minOf { stayID ->
                        val stay = objects[stayID]
                        BoundsUtils.maxMove(stay.bounds.red, move_bs, stay.stats.POS, obj.stats.POS, move_p)
                    }
                }

                return rangeX.minOf { ix -> rangeY.minOf { iy -> maxPathIn(ix, iy) } }
            }

            fun move(move_p: PointN): Double {
                val mm = maxMove(move_p)
                obj.stats.POS += move_p*mm*(1 - ConstL.LITTLE)
                return mm
            }

            val min_d = move(obj.stats.nPOS)
            obj.stats.fly = min_d == 1.0
            val np = obj.stats.nPOS*(1 - min_d)
            (0 until np.dimension()).forEach { move(np.separate(it)) }

            obj.stats.nPOS = PointN.ZERO
        }
    }

    private fun nextActivate() {
        //todo less code

        fun setActivate(
            o1: GameObject,
            sh1: BoundsElement,
            o2: GameObject,
            sh2: BoundsElement,
            code: InputAction.CODE,
            message: String,
        ) {
            val shape1 = sh1.shape() ?: return
            val shape2 = sh2.shape() ?: return
            if(ShapeUtils.into(shape1.copy(o1.stats.POS), shape2.copy(o2.stats.POS))) {
                o1.activate(InputAction(code, "$message | ${sh1.name} ${sh2.name}", o2))
            }
        }

        for(blueObjID in objects.indices) {
            val blueObj = objects[blueObjID]
            if(blueObj.tagged("#inactive")) continue

            //cell_range[BoundsBox.index("BLUE")]

            val blueBounds = blueObj.bounds.blue
            if(blueBounds.isEmpty()) continue
            for(mainObj in objects) {
                if(mainObj.tagged("#inactive")) continue
                val mainBounds = mainObj.bounds.main
                if(mainBounds.isEmpty()) continue
                blueBounds.elements.forEach { blueElement ->
                    mainBounds.elements.forEach { mainElement ->
                        setActivate(
                            blueObj,
                            blueElement,
                            mainObj,
                            mainElement,
                            InputAction.CODE.INTERRUPT,
                            "#interrupt")
                        setActivate(
                            mainObj,
                            mainElement,
                            blueObj,
                            blueElement,
                            InputAction.CODE.INTERRUPT_I,
                            "#interrupt_I")
                    }
                }
            }
        }

        for(mainObj in objects) {
            if(!mainObj.interact() || mainObj.tagged("#inactive")) continue
            val mainBounds = mainObj.bounds.main
            if(mainBounds.isEmpty()) continue
            for(greenObj in objects) {
                if(greenObj.tagged("#inactive")) continue
                val greenBounds = greenObj.bounds.green
                if(greenBounds.isEmpty()) continue
                greenBounds.elements.forEach { greenElement ->
                    mainBounds.elements.forEach { mainElement ->
                        setActivate(
                            greenObj,
                            greenElement,
                            mainObj,
                            mainElement,
                            InputAction.CODE.INTERACT,
                            "#interact")
                        setActivate(
                            mainObj,
                            mainElement,
                            greenObj,
                            greenElement,
                            InputAction.CODE.INTERACT_I,
                            "#interact_I")
                    }
                }
            }
        }
        for(obj1 in objects) {
            if(obj1.tagged("#inactive")) continue
            val bounds1 = obj1.bounds.orange
            if(bounds1.isEmpty()) continue
            for(obj2 in objects) {
                if(obj1 == obj2 || obj2.tagged("#inactive")) continue
                val bounds2 = obj2.bounds.orange
                if(bounds2.isEmpty()) continue
                bounds2.elements.forEach { element2 ->
                    bounds1.elements.forEach { element1 ->
                        setActivate(obj1, element1, obj2, element2, InputAction.CODE.IMPACT, "#impact")
                    }
                }
            }
        }
    }

    override fun toString(): String {
        val wr = StringBuilder()
        wr.append("//Uzery Game Studio 2019-2023\n")
        wr.append("//last edit: ${FGUtils.time_YMD()} ${FGUtils.time_HMS()}\n\n")

        wr.append("room: ${PosValue(pos)} ${SizeValue(size)}\n\n")

        for(o in objects) {
            o.setValues()
            if(o.name != "temp" && o.name != "temporary") wr.append("$o\n")
        }

        return wr.toString()
    }
}
