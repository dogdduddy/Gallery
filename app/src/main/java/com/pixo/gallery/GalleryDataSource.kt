package com.pixo.gallery

import androidx.paging.PagingSource

interface GalleryDataSource {

    suspend fun fetchAlbums(): List<Album>
    fun fetchPhotos(albumId: String): PagingSource<Int, Photo>

    fun reset()

}