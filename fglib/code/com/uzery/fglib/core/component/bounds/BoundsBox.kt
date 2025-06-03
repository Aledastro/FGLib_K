package com.uzery.fglib.core.component.bounds

/**
 * todo doc
 **/
class BoundsBox(bounds: ArrayList<BoundsComponent>) {
    private val map = HashMap<String, Bounds>()
    val codes = ArrayList<String>()

    init {
        for (bs in bounds) {
            if (map[bs.code] == null) {
                map[bs.code] = Bounds()
            }
            map[bs.code]!!.add(bs.element)
        }

        codes.addAll(map.keys)
    }

    operator fun get(code: String) = map[code] ?: Bounds()

    fun isEmpty(code: String) = map[code]?.empty ?: true

    fun name(index: Int) = codes[index]
    fun index(name: String) = codes.indexOf(name)

    val SIZE = map.keys.size

    val indices
        get() = map.keys.indices
}
