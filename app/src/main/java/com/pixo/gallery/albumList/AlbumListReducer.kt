package com.pixo.gallery.albumList

import com.pixo.gallery.Album
import com.pixo.gallery.Reducer
import javax.inject.Inject

class AlbumListReducer @Inject constructor() : Reducer<AlbumListMutation, AlbumListUiState> {
        override fun invoke(
        mutation: AlbumListMutation,
        currentState: AlbumListUiState
    ): AlbumListUiState {
        return when(mutation) {
            AlbumListMutation.ShowLostConnection -> mutateToLostConnection()
            AlbumListMutation.ShowLoader -> mutateToShowLoader()
            is AlbumListMutation.ShowAlbumList -> mutateToShowAlbumList(
                mutation.albums
            )
            is AlbumListMutation.ShowError -> mutateToShowError(
                mutation.message
            )
        }
    }

    private fun mutateToShowError(message: String): AlbumListUiState {
        return AlbumListUiState.Error(message)
    }

    private fun mutateToShowAlbumList(albumList: List<Album>): AlbumListUiState.Content {
        return AlbumListUiState.Content(albumList)
    }

    private fun mutateToLostConnection(): AlbumListUiState {
        return AlbumListUiState.Error("")
    }

    private fun mutateToShowLoader(): AlbumListUiState {
        return AlbumListUiState.Loading
    }
}