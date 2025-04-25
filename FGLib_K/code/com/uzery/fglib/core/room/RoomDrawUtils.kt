package com.uzery.fglib.core.room

import com.uzery.fglib.core.component.visual.Visualiser
import com.uzery.fglib.core.obj.GameObject
import com.uzery.fglib.core.program.Platform.render_camera
import com.uzery.fglib.utils.math.geom.PointN
import kotlin.math.sign

/**
 * TODO("doc")
 **/
object RoomDrawUtils {
    fun drawVisuals(
        draw_pos: PointN,
        vis: ArrayList<Visualiser>,
        pos_map: HashMap<Visualiser, PointN>,
        sort_map: HashMap<Visualiser, PointN>
    ) {
        vis.sortWith { v1, v2 ->
            val c = v1.compareTo(v2)
            if (c != 0) c else render_camera.sort(sort_map[v1]!!, sort_map[v2]!!)
        }
        vis.forEach { visual ->
            visual.drawWithDefaults(draw_pos+render_camera[pos_map[visual]!!])
        }
    }

    fun addObjVis(
        vis: ArrayList<Visualiser>,
        pos_map: HashMap<Visualiser, PointN>,
        sort_map: HashMap<Visualiser, PointN>,
        obj: GameObject
    ) {
        vis.addAll(obj.visuals)

        obj.visuals.forEach { v ->
            pos_map[v] = obj.pos_with_owners+obj.main_owner.stats.roomPOS
        }
        obj.visuals.forEach { v ->
            val sort_p = v.sortPOS+obj.stats.sortPOS
            sort_map[v] = pos_map[v]!!+sort_p
        }

        obj.followers.forEach { addObjVis(vis, pos_map, sort_map, it) }
    }
}
