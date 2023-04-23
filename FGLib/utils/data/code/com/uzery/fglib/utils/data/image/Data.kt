package com.uzery.fglib.utils.data.image

import javafx.scene.image.Image

class Data {
    companion object {
        private val map=HashMap<String,Image>()
        fun get(filename: String): Image {
            val input=map[filename]
            return if(input==null){
                val img=Image(filename)
                map[filename] = img
                img
            }else input
        }
    }


}
