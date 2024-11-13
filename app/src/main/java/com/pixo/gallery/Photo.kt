package com.pixo.gallery

data class Photo(
    val id: Int,
    val uri: String,
    val date: Long,
    val size: Long,
    val isSelected: Boolean = false
)