package com.pixo.gallery.albumList

import com.pixo.gallery.Test

sealed interface AlbumListMutation {
    data object ShowLostConnection: AlbumListMutation
    data object ShowLoader : AlbumListMutation
    data class ShowAlbumList(val albums: List<Test>) : AlbumListMutation
    data class ShowError(val message: String) : AlbumListMutation
}