package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.num.StringN
import javafx.scene.paint.Color
import java.util.*

abstract class ClassGetter<Type> {

    operator fun get(name: String, args: ArrayList<ArrayList<String>>): Type = getMark(name, args).invoke()
    operator fun get(input: String): Type = getMark(FGFormat[input].first, FGFormat[input].second).invoke()
    fun getEntryName(id: Int) = map.keys.elementAt(id)

    fun getEntry(id: Int): () -> Type {
        no_info = true
        return map[getEntryName(id)] ?: throw DebugData.error("wrong id: $id")
    }

    fun entry_size() = map.keys.size

    fun getMark(name: String, args: ArrayList<ArrayList<String>>): () -> Type {
        input = args
        in_id = 0
        no_info = false
        return map[StringN(name, args.size)] ?: throw DebugData.error("ERROR getMark(): $name | $args")
    }


    ////////////////////////////////////////////////////////////////////////////////
    protected var no_info = false
    private val map: TreeMap<StringN, () -> Type> = TreeMap<StringN, () -> Type>()

    init {
        this.addAll()
    }

    protected abstract fun addAll()


    protected fun add(sn: StringN, mark: () -> Type) {
        map[sn] = mark
    }

    protected fun add(s: String, n: Int, mark: () -> Type) {
        map[StringN(s, n)] = mark
    }

    protected fun add(s: String, mark: () -> Type) {
        map[StringN(s)] = mark
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
