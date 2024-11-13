package com.pixo.gallery.albumList

sealed class AlbumListUiIntent {
    data object FetchAlbums : AlbumListUiIntent()
    data class AlbumClicked(val albumId: String) : AlbumListUiIntent()
}
