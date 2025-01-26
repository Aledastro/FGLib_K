package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.data.entry.FGEntry
import com.uzery.fglib.utils.graphics.data.FGColor
import com.uzery.fglib.utils.math.geom.Direct
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.solve.MathSolveUtils
import com.uzery.fglib.utils.struct.num.IntI
import com.uzery.fglib.utils.struct.num.StringN

/**
 * TODO("doc")
 **/
abstract class ClassGetter<Type>: AbstractClassGetter<Type>() {
    protected var no_info = false
        private set
    private val map: ArrayList<Pair<StringN, () -> Type>> = ArrayList()

    final override fun entries_size(): Int {
        return map.size
    }

    final override fun getEntry(id: Int): () -> Type {
        return {
            no_info = true
            map[id].second()
        }
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

    override fun getMark(entry: FGEntry): () -> Type {
        return {
            input = entry.args
            in_id = 0
            no_info = false
            val first = map.firstOrNull { it.first == StringN(entry.name, entry.args.size) }
                ?: throw DebugData.error("no entry from $entry")

            first.second()
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected fun stringX(i: Int) = if (no_info) "" else input[in_id-1][i]
    protected fun intX(i: Int) = if (no_info) 0 else MathSolveUtils.solveInt(input[in_id-1][i])
    protected fun doubleX(i: Int) = if (no_info) 0.0 else MathSolveUtils.solveDouble(input[in_id-1][i])
    protected fun longX(i: Int) = if (no_info) 0L else MathSolveUtils.solveLong(input[in_id-1][i])
    protected fun boolX(i: Int) = if (no_info) false else input[in_id-1][i].toBoolean()

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private var in_id = 0
    private lateinit var input: Array<Array<String>>
    protected val string: String
        get() = if (no_info) "" else input[in_id++][0]
    protected val int: Int
        get() = if (no_info) 0 else MathSolveUtils.solveInt(input[in_id++][0])
    protected val double: Double
        get() = if (no_info) 0.0 else MathSolveUtils.solveDouble(input[in_id++][0])
    protected val long: Long
        get() = if (no_info) 0L else MathSolveUtils.solveLong(input[in_id++][0])
    protected val boolean: Boolean
        get() = if (no_info) false else input[in_id++][0].toBoolean()

    protected val color: FGColor
        get() = if (no_info) FGColor.BLACK else FGColor(double, doubleX(1), doubleX(2), doubleX(3))
    protected val pos: PointN
        get() = when {
            no_info || input[in_id][0] == "ZERO" -> PointN.ZERO
            else -> PointN(Array(input[in_id++].size) { i -> doubleX(i) })
        }

    protected val size: PointN
        get() = pos
    protected val intI: IntI
        get() = if (no_info) IntI(0, 0) else IntI(int, intX(1))
    protected val direct: Direct
        get() = if (no_info) Direct.CENTER else Direct.valueOf(string)
}
