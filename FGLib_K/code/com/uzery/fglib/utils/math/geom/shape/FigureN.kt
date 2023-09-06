package com.uzery.fglib.utils.math.geom.shape

import com.uzery.fglib.utils.math.geom.FieldN
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.geom.Shape
import java.util.*

open class FigureN(val fields: List<FieldN>): Shape() {
    constructor(pos: PointN, vararg fieldN: FieldN): this(fieldN.toList())


    /*val mirage_fields
        get(): LinkedList<FieldN>{
            val list = LinkedList<FieldN>()
            fields.forEach { f -> list.add(f.copy(pos)) }
            return list
        }*/

    override fun copy(move: PointN): Shape {
        val list = LinkedList<FieldN>()
        fields.forEach { field -> list.add(field.copy(move)) }
        return FigureN(list)
    }

    override fun into(pos: PointN): Boolean {
        return fields.all { field -> field.intoHalf(pos) }
    }
    private fun intoS(pos: PointN): Boolean {
        return fields.all { field -> field.intoHalfS(pos) }
    }

    val current_pos = LinkedList<PointN>()
    private var init_current_pos = false

    override val L = getLP()
    override val R = getRP()

    private fun getFields() {
        val dim = fields[0].dim
        val current_fields = LinkedList<FieldN>()
        current_fields.addAll(fields)
        //todo it don't work well for large dim>3
        for (d in 0 until dim-1) {
            val list = LinkedList<FieldN>()

            for (i in 0 until current_fields.size) {
                for (j in i+1 until current_fields.size) {
                    list.add(current_fields[i]*current_fields[j])
                }
            }
            list.removeIf { !it.exists() }
            current_fields.clear()
            current_fields.addAll(list)
        }
        current_fields.forEach { f -> f.solve()?.let { p -> current_pos.add(p) } }
        current_pos.removeIf { p -> !intoS(p) }
        init_current_pos = true
    }

    private fun getLP(): PointN {
        if (!init_current_pos) getFields()

        val dim = fields[0].dim
        if (current_pos.isEmpty()) return PointN(Array(dim){1.0})

        return PointN(Array(dim) { level -> current_pos.minOf { it[level] } })
    }

    private fun getRP(): PointN {
        if (!init_current_pos) getFields()

        val dim = fields[0].dim
        if (current_pos.isEmpty()) return PointN(Array(dim){-1.0})

        return PointN(Array(dim) { level -> current_pos.maxOf { it[level] } })
    }

    override val code = Code.FIGURE

    operator fun times(other: FigureN): FigureN {
        val res = LinkedList<FieldN>()

        this.fields.forEach { f -> res.add(f.copy(PointN.ZERO)) }
        other.fields.forEach { f -> res.add(f.copy(PointN.ZERO)) }

//        println(res)
//        println()
//        println(this.fields)
//        println()
//        println(other.fields)

        return FigureN(res)
    }

    override fun toString(): String {
        return "figure[$fields]"
    }

    fun exists(): Boolean {
        if (!S.more(PointN.ZERO)) return false

        return fields.all { field -> field.exists() }
    }
}
