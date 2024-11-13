package com.pixo.gallery.albumList

import com.pixo.gallery.Album

sealed interface AlbumListMutation {
    data object ShowLostConnection: AlbumListMutation
    data object ShowLoader : AlbumListMutation
    data class ShowAlbumList(val albums: List<Album>) : AlbumListMutation
    data class ShowError(val message: String) : AlbumListMutation
}