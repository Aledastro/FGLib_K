package com.uzery.fglib.core.component.bounds

/**
 * todo doc
 **/
class CollisionMap(collisions: List<CollisionComponent>) {
    constructor(): this(ArrayList())

    private val map = HashMap<BoundsCode, ArrayList<CollisionComponent>>()
    val codes = ArrayList<BoundsCode>()

    init {
        for (c in collisions) {
            if (map[c.code] == null) {
                map[c.code] = ArrayList()
            }
            map[c.code]!!.add(c)
        }

        codes.addAll(map.keys)
    }

    operator fun get(code: BoundsCode) = map[code] ?: ArrayList()

    fun isEmpty(code: BoundsCode) = map[code]?.isEmpty() ?: true

    fun codeOf(index: Int) = codes[index]
    fun indexOf(name: BoundsCode) = codes.indexOf(name)

    val size = map.keys.size

    val indices
        get() = map.keys.indices
}
