package com.pixo.gallery.albumList

import com.pixo.gallery.Album

sealed interface AlbumListUiState {
    data object Loading : AlbumListUiState
    data object Empty : AlbumListUiState
    data class Content(val albums: List<Album>) : AlbumListUiState
    data class Error(val message: String) : AlbumListUiState
}