package com.uzery.fglib.utils.data.getter

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.geom.PointN
import com.uzery.fglib.utils.math.num.StringN
import javafx.scene.paint.Color
import java.util.*

abstract class AbstractClassGetter<Type> {
    operator fun get(name: String, args: ArrayList<ArrayList<String>>): Type = getMark(name, args).invoke()
    operator fun get(input: String): Type = get(FGFormat[input].first, FGFormat[input].second)

    ///////////////////////////////////////////////////////////////////////////////////////////

    abstract fun getMark(name: String, args: ArrayList<ArrayList<String>>): () -> Type

    abstract fun getEntryName(id: Int): StringN

    abstract fun getEntry(id: Int): () -> Type

    abstract fun entries_size(): Int
}
