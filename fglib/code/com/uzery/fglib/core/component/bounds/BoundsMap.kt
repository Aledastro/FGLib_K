package com.uzery.fglib.core.component.bounds

/**
 * todo doc
 **/
class BoundsMap(bounds: ArrayList<BoundsComponent>) {
    private val map = HashMap<BoundsCode, Bounds>()
    val codes = ArrayList<BoundsCode>()

    init {
        for (bs in bounds) {
            if (map[bs.code] == null) {
                map[bs.code] = Bounds()
                map[bs.code]!!.color = bs.code.color
            }
            map[bs.code]!!.add(bs.element)
        }

        codes.addAll(map.keys)

        for (code in codes) {
            map[code]!!.next()
        }
    }

    operator fun get(code: BoundsCode) = map[code] ?: Bounds()

    fun isEmpty(code: BoundsCode) = map[code]?.empty ?: true

    fun codeOf(index: Int) = codes[index]
    fun indexOf(name: BoundsCode) = codes.indexOf(name)

    val SIZE = map.keys.size

    val indices
        get() = map.keys.indices
}
