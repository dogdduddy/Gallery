package com.pixo.gallery.albumList

import com.pixo.gallery.Test

sealed interface AlbumListUiState {
    data object Loading : AlbumListUiState
    data object Empty : AlbumListUiState
    data class Content(val albums: List<Test>) : AlbumListUiState
    data class Error(val message: String) : AlbumListUiState
}