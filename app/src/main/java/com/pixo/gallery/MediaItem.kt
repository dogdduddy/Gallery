package com.pixo.gallery

data class MediaItem(
    val id: Int,
    val uri: String,
    val albumId: Int,
    val albumName: String,
    val date: Long,
    val size: Long
) {
    fun toPhoto(): Photo {
        return Photo(id, uri, date, size)
    }

    fun toAlbum(): Album {
        return Album(albumId, albumName, coverUri = uri, photosCount = 1)
    }
}