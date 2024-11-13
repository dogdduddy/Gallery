package com.pixo.gallery.albumList

import com.pixo.gallery.Reducer
import com.pixo.gallery.Test
import javax.inject.Inject

class AlbumListReducer @Inject constructor() : Reducer<AlbumListMutation, AlbumListUiState> {
        override fun invoke(
        mutation: AlbumListMutation,
        currentState: AlbumListUiState
    ): AlbumListUiState {
        return when(mutation) {
            AlbumListMutation.ShowLostConnection -> mutateToLostConnection()
            AlbumListMutation.ShowLoader -> mutateToShowLoader()
            is AlbumListMutation.ShowAlbumList -> mutateToShowTodoList(
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

    private fun mutateToShowTodoList(todoList: List<Test>): AlbumListUiState.Content {
        return AlbumListUiState.Content(todoList)
    }

    private fun mutateToLostConnection(): AlbumListUiState {
        return AlbumListUiState.Error("")
    }

    private fun mutateToShowLoader(): AlbumListUiState {
        return AlbumListUiState.Loading
    }
}