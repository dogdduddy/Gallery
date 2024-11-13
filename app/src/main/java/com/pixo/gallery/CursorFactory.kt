package com.pixo.gallery

import android.content.Context
import android.database.Cursor
import android.net.Uri

interface CursorFactory {
    fun create(context: Context, album: String): Cursor?

    fun getContentUri() : Uri

    fun getProjection() : Array<String>

    fun createMediaItem(cursor: Cursor) : MediaItem
}