package com.example.startallplayer

import java.io.Serializable

class MediaItem : Serializable {
    var name: String? = null
    var duration: Long = 0
    var size: Long = 0
    var data: String? = null
    var artist: String? = null
    var desc: String? = null
    var imageUrl: String? = null

    override fun toString(): String {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", desc='" + desc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}'
    }
}