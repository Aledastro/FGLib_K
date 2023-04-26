package com.uzery.fglib.utils.math.getter

import com.uzery.fglib.core.obj.DrawLayer
import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.num.StringN
import javafx.scene.paint.Color
import java.util.*
import java.util.function.Supplier

abstract class ClassGetterInstance<Type> {
    private val map: TreeMap<StringN, Supplier<Type>> = TreeMap<StringN, Supplier<Type>>()

    //todo can be overrated
    init {
        this.addAll()
    }

    protected abstract fun addAll()
    fun getMark(name: StringN, args: ArrayList<ArrayList<String>>): Supplier<Type> {
        input = args
        in_id = 0
        return map[name] ?: throw DebugData.error("ERROR getMark(): $name | $args")
    }

    protected fun add(sn: StringN, mark: Supplier<Type>) {
        map[sn] = mark
    }

    protected fun add(s: String, n: Int, mark: Supplier<Type>) {
        map[StringN(s, n)] = mark
    }

    protected fun add(s: String, mark: Supplier<Type>) {
        map[StringN(s)] = mark
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun stringX(i: Int) = input[in_id - 1][i]
    private fun intX(i: Int) = input[in_id - 1][i].toInt()
    private fun doubleX(i: Int) = input[in_id - 1][i].toDouble()
    private fun longX(i: Int) = input[in_id - 1][i].toLong()
    private fun boolX(i: Int) = input[in_id - 1][i].toBoolean()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var in_id = 0
    private var input = ArrayList<ArrayList<String>>()
    protected val string: String
        get() = input[in_id++][0]
    protected val int: Int
        get() = input[in_id++][0].toInt()
    protected val double: Double
        get() = input[in_id++][0].toDouble()
    protected val long: Long
        get() = input[in_id++][0].toLong()
    protected val boolean: Boolean
        get() = input[in_id++][0].toBoolean()
    protected val layer: DrawLayer
        get() = DrawLayer(double)

    /*protected val type: TypeValue
        get() {
            val i = in_id++
            return if(input[i].size == 1) TypeValue(input[i][0]) else TypeValue(string, int,int,int,int)
        }*/
    protected val color256: Color
        get() = Color.rgb(int, intX(1), intX(2), intX(3)*1.0/255)
    protected val color: Color
        get() = Color.color(double, doubleX(1), doubleX(2), doubleX(3))
    protected val pos: PointN
        get() = PointN(Array(input[in_id++].size) { i -> doubleX(i) })
    protected val size: PointN
        get() = PointN(Array(input[in_id++].size) { i -> doubleX(i) })
}