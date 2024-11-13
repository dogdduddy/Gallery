package com.pixo.gallery

import android.content.Context
import android.database.Cursor
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GalleryDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cursorFactory: ImageCursorFactory
): GalleryDataSource {

    private var cursor: Cursor? = null

    override fun reset() {
        cursor?.close()
    }

    override suspend fun fetchAlbums(): List<Album> {

        cursor = cursorFactory.create(context, "")

        val albumList = ArrayList<Album>()

        cursor?.use { c ->
            while (c.moveToNext()) {
                val item = cursorFactory.createMediaItem(c)

                if(albumList.any { it.id == item.albumId }) {
                    albumList.find { it.id == item.albumId }?.let {
                        it.photosCount++
                    }
                } else {
                    albumList.add(item.toAlbum())
                }
            }
        }

        cursor?.close()
        return albumList
    }

    override fun fetchPhotos(albumId: String): PagingSource<Int, Photo> {
        cursor = cursorFactory.create(context, albumId)
        return object : PagingSource<Int, Photo>() {

            override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
                return try {

                    val currentPage = params.key ?: 0
                    val data = ArrayList<Photo>()

                    cursor?.let { c ->
                        data.addAll(getMediaList(c, params.loadSize))
                    }

                    val prevPage = if (currentPage == 0) null else currentPage - 1
                    val nextPage = if (data.size < params.loadSize) null else currentPage + 1
                    LoadResult.Page(data, prevPage, nextPage)
                } catch (e: Exception) {
                    cursor?.close()
                    LoadResult.Error(e)
                }
            }

        }
    }

    private fun getMediaList(cursor: Cursor?, loadSize: Int): ArrayList<Photo> {
        val photoList = ArrayList<Photo>()
        cursor?.let { c ->
            for (i in 0 until loadSize) {
                if (!c.moveToNext()) {
                    break
                }
                photoList.add(cursorFactory.createMediaItem(c).toPhoto())
            }
            return photoList
        } ?: run {
            return photoList
        }
    }
}