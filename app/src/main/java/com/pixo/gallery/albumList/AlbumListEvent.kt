package com.pixo.gallery.albumList


sealed interface AlbumListEvent {
    data class NavigateToAlbumDetail(val albumId: String) : AlbumListEvent

    data class Toast(val text: String): AlbumListEvent

}