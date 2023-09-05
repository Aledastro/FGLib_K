package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import java.util.*

open class FigureN(val pos: PointN, val fields: List<FieldN>): Shape() {
    constructor(pos: PointN, vararg fieldN: FieldN): this(pos, fieldN.toList())

    override fun copy(move: PointN): Shape {
        val list = LinkedList<FieldN>()
        fields.forEach { field -> list.add(field.copy()) }
        return FigureN(pos+move, list)
    }

    override fun into(pos: PointN): Boolean {
        return fields.all { field -> field.intoHalf(pos) }
    }

    val current_pos = LinkedList<PointN>()
    private var init_current_pos = false

    override val L = pos+getLP()
    override val R = pos+getRP()

    private fun getFields(){
        val dim = fields[0].dim
        val current_fields = LinkedList<FieldN>()
        current_fields.addAll(fields)
        //todo it don't work well for large dim>3
        for (d in 0 until dim-1){
            val list = LinkedList<FieldN>()

            for (i in current_fields.indices){
                for (j in i+1 until current_fields.size){
                    list.add(current_fields[i]*current_fields[j])
                }
            }
            list.removeIf { !it.exists() }
            current_fields.clear()
            current_fields.addAll(list)
        }
        current_fields.forEach { f ->f.solve()?.let { p->current_pos.add(p) } }
        init_current_pos = true
    }
    private fun getLP(): PointN {
        if(!init_current_pos) getFields()

        val dim = fields[0].dim
        if(current_pos.isEmpty()) return PointN.ZERO

        return PointN(Array(dim) { level -> current_pos.minOf { it[level] } })
    }

    private fun getRP(): PointN {
        if(!init_current_pos) getFields()

        val dim = fields[0].dim
        if(current_pos.isEmpty()) return PointN.ZERO

        return PointN(Array(dim) { level -> current_pos.maxOf { it[level] } })
    }

    override val code = Code.FIGURE

    operator fun times(other: FigureN): FigureN {
        val moveThis = LinkedList<FieldN>()
        moveThis.addAll(this.fields)
        moveThis.indices.forEach { i -> moveThis[i] = moveThis[i].copy(this.pos) }

        val moveOther = LinkedList<FieldN>()
        moveOther.addAll(other.fields)
        moveOther.indices.forEach { i -> moveOther[i] = moveOther[i].copy(other.pos) }

        val res = LinkedList<FieldN>()
        res.addAll(moveThis)
        res.addAll(moveOther)

        return FigureN(PointN.ZERO, res)
    }

    override fun toString(): String {
        return "figure[$pos, $fields]"
    }

    fun exists(): Boolean {
        if(S.less(PointN.ZERO)) return false

        if (fields.any { field -> !field.exists() }) return false
        return true
    }
}
