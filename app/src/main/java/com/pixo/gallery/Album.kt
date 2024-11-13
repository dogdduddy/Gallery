package com.pixo.gallery

data class Album(
    val id: Int,
    val name: String,
    var folderName: String? = null,
    val coverUri: String,
    var photosCount: Int
)