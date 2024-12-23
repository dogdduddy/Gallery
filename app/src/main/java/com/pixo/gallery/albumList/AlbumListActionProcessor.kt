package com.pixo.gallery.albumList

import com.pixo.gallery.ActionProcessor
import com.pixo.gallery.repository.GalleryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AlbumListActionProcessor  @Inject constructor(
    private val repository: GalleryRepository
): ActionProcessor<AlbumListUiIntent, AlbumListMutation, AlbumListEvent> {

    override suspend fun invoke(intent: AlbumListUiIntent): Flow<Pair<AlbumListMutation?, AlbumListEvent?>> {
        return when(intent) {
            is AlbumListUiIntent.FetchAlbums -> loadAlbums()
            is AlbumListUiIntent.AlbumClicked -> albumClicked(intent.albumId)
        }
    }

    private suspend fun loadAlbums(): Flow<Pair<AlbumListMutation?, AlbumListEvent?>> = flow {
        emit(AlbumListMutation.ShowLoader to null)
        try {
            val albumList = repository.getAlbumList()
            emit(AlbumListMutation.ShowAlbumList(albumList) to null)
        } catch (e: Exception) {
            emit(AlbumListMutation.ShowError(e.message ?: "Unknown error") to null)
        }
    }

    private fun albumClicked(albumId: String): Flow<Pair<AlbumListMutation?, AlbumListEvent?>> = flow {
        emit( null to AlbumListEvent.NavigateToAlbumDetail(albumId))
    }
}