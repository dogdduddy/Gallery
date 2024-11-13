package com.pixo.gallery.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.pixo.gallery.Album
import com.pixo.gallery.GalleryDataSource
import com.pixo.gallery.MediaItem
import com.pixo.gallery.Photo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleryRepository @Inject constructor(
    private val dataSource: GalleryDataSource
) {
    suspend fun getAlbumList(): List<Album> = dataSource.fetchAlbums()

    suspend fun getPhotoList(albumId: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            )
        ) {
            dataSource.fetchPhotos(albumId)
        }.flow
    }

}