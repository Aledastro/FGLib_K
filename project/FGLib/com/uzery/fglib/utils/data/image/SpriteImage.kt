package com.uzery.fglib.utils.data.image

import com.uzery.fglib.utils.data.debug.DebugData
import com.uzery.fglib.utils.math.num.IntI
import javafx.scene.image.Image
import javafx.scene.image.WritableImage

class SpriteImage(private val filename: String, val size: IntI, scale: Int = -1) {
    private val data = HashMap<IntI, WritableImage>()
    private val count: IntI

    init {
        //todo if scale=-1
        val read = Image(filename)
        val origin_size = IntI(read.width.toInt(), read.height.toInt())
        count = origin_size/size
        for(i in 0 until count.n) {
            for(j in 0 until count.m) {
                val img = WritableImage(size.n*scale, size.m*scale)
                img.pixelWriter.setPixels(0, 0, size.n, size.m, read.pixelReader, i*size.n, j*size.m)
                if(scale == 1) {
                    data[IntI(i, j)] = img
                    continue
                }
                val res = WritableImage(size.n*scale, size.m*scale)
                for(si in 0 until size.n*scale) {
                    for(sj in 0 until size.m*scale) {
                        res.pixelWriter.setArgb(si, sj, img.pixelReader.getArgb(si/scale, sj/scale))
                    }
                }
                data[IntI(i, j)] = res
            }
        }

    }

    fun get(pos: IntI): Image {
        return data[pos] ?: throw DebugData.error("illegal get from $filename $pos")
    }

}
