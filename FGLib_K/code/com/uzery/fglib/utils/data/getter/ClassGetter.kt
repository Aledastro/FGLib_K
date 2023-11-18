package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.num.StringN
import javafx.scene.paint.Color
import java.util.*

abstract class ClassGetter<Type>: AbstractClassGetter<Type>() {
    protected var no_info = false
    private val map: ArrayList<Pair<StringN, () -> Type>> = ArrayList()

    final override fun entries_size(): Int {
        return map.size
    }

    final override fun getEntry(id: Int): () -> Type {
        no_info = true
        return map[id].second
    }

    final override fun getEntryName(id: Int): StringN {
        return map[id].first
    }

    operator fun contains(input: StringN): Boolean {
        return map.any { it.first == input }
    }


    init {
        this.addAll()
    }

    protected abstract fun addAll()


    protected fun add(sn: StringN, mark: () -> Type) {
        if (sn in this) return

        map.add(Pair(sn, mark))
    }

    protected fun add(s: String, n: Int, mark: () -> Type) {
        add(StringN(s, n), mark)
    }

    protected fun add(s: String, mark: () -> Type) {
        add(StringN(s), mark)
    }

    override fun getMark(name: String, args: ArrayList<ArrayList<String>>): () -> Type {
        input = args
        in_id = 0
        no_info = false
        return map.first { it.first == StringN(name, args.size) }.second
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected fun stringX(i: Int) = if (no_info) "" else input[in_id-1][i]
    protected fun intX(i: Int) = if (no_info) 0 else input[in_id-1][i].toInt()
    protected fun doubleX(i: Int) = if (no_info) 0.0 else input[in_id-1][i].toDouble()
    protected fun longX(i: Int) = if (no_info) 0L else input[in_id-1][i].toLong()
    protected fun boolX(i: Int) = if (no_info) false else input[in_id-1][i].toBoolean()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var in_id = 0
    private var input = ArrayList<ArrayList<String>>()
    protected val string: String
        get() = if (no_info) "" else input[in_id++][0]
    protected val int: Int
        get() = if (no_info) 0 else input[in_id++][0].toInt()
    protected val double: Double
        get() = if (no_info) 0.0 else input[in_id++][0].toDouble()
    protected val long: Long
        get() = if (no_info) 0L else input[in_id++][0].toLong()
    protected val boolean: Boolean
        get() = if (no_info) false else input[in_id++][0].toBoolean()

    protected val color: Color
        get() = if (no_info) Color.BLACK else Color.color(double, doubleX(1), doubleX(2), doubleX(3))
    protected val pos: PointN
        get() {
            return if (no_info || input[in_id][0] == "ZERO") PointN.ZERO
            else PointN(Array(input[in_id++].size) { i -> doubleX(i) })
        }
    protected val size: PointN
        get() = pos
}
