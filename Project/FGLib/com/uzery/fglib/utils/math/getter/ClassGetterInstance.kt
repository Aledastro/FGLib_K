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
        if(!map.containsKey(name)) throw DebugData.error("ERROR getMark(): $name | $args")
        return map[name]!!
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

    private val stringX: String
        get() = input[in_id - 1][0]
    private val intX: Int
        get() = input[in_id - 1][0].toInt()
    private val doubleX: Double
        get() = input[in_id - 1][0].toDouble()
    private val longX: Long
        get() = input[in_id - 1][0].toLong()
    private val booleanX: Boolean
        get() = java.lang.Boolean.parseBoolean(input[in_id - 1][0])

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
        get() = java.lang.Boolean.parseBoolean(input[in_id++][0])
    protected val layer: DrawLayer
        get() = DrawLayer(double)

    /*protected val type: TypeValue
        get() {
            val i = in_id++
            return if(input[i].size == 1) TypeValue(input[i][0]) else TypeValue(string, int,int,int,int)
        }*/
    protected val color256: Color
        get() = Color.rgb(int, intX, intX, intX*1.0/255)
    protected val color: Color
        get() = Color.color(double, doubleX, doubleX, doubleX)
    protected val pos: PointN
        get(): PointN {
            return PointN(Array(input[in_id++].size) { doubleX })
        }
    protected val size: PointN
        get() = PointN(Array(input[in_id++].size) { doubleX })
}