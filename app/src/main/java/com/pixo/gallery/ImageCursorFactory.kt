package com.pixo.gallery

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import javax.inject.Inject

class ImageCursorFactory @Inject constructor(): CursorFactory {
    private val ALL_ALBUM = "ALL_ALBUM"

    override fun create(context: Context, albumId: String): Cursor? {
        val projection = getProjection()
        val mediaUri = getContentUri()
        val selectionArgs = null
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if(albumId.isNullOrEmpty() || albumId == ALL_ALBUM)
                    "${MediaStore.Images.Media.SIZE} > 0"
                else
                    "${MediaStore.Images.Media.SIZE} > 0 AND ${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = '$albumId'"
            }
            else null

        val sortOrder = String.format("%s %s", MediaStore.Images.Media.DATE_ADDED, "desc")

        return context.contentResolver.query(mediaUri, projection, selection, selectionArgs, sortOrder)
    }

    override fun getContentUri(): Uri {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    override fun getProjection(): Array<String> {
        return arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.TITLE
        )
    }

    override fun createMediaItem(cursor: Cursor): MediaItem {
        val id = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val dateAdded = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
        val bucketId = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)
        val fileSize = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
        val albumName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val albumTitle = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)
        val url = ContentUris.withAppendedId(getContentUri(),cursor.getLong(id))

        return MediaItem(
            id = cursor.getInt(id),
            uri = url.toString(),
            albumId = cursor.getInt(bucketId),
            albumName = cursor.getString(albumName) ?: cursor.getString(albumTitle),
            date = cursor.getLong(dateAdded),
            size = cursor.getLong(fileSize),
        )
    }
}