package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntInt
import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class SpriteImage(private val filename: String, val size: IntInt) {
    private val data = HashMap<IntInt, WritableImage>()
    private val count: IntInt

    init {
        val read = Image(filename)
        val origin_size = IntInt(read.width.toInt(), read.height.toInt())
        count = origin_size/size
        for(i in 0 until count.n) {
            for(j in 0 until count.m) {
                data[IntInt(i, j)] = WritableImage(size.n, size.m)
                data[IntInt(i, j)]!!.pixelWriter.setPixels(0, 0, size.n, size.m, read.pixelReader, i*size.n, j*size.m)
            }
        }

    }

    fun get(pos: IntInt): Image {
        return data[pos] ?: throw DebugData.error("illegal get from $filename $pos")
    }

}
