package com.pixo.gallery

import com.pixo.gallery.albumList.AlbumListActionProcessor
import com.pixo.gallery.albumList.AlbumListEvent
import com.pixo.gallery.albumList.AlbumListMutation
import com.pixo.gallery.albumList.AlbumListReducer
import com.pixo.gallery.albumList.AlbumListUiIntent
import com.pixo.gallery.albumList.AlbumListUiState
import com.pixo.gallery.repository.GalleryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlbumModule {

    @Binds
    abstract fun bindActionProcessor(albumListActionProcessor: AlbumListActionProcessor): ActionProcessor<AlbumListUiIntent, AlbumListMutation, AlbumListEvent>

    @Binds
    abstract fun bindAlbumListReducer(albumListReducer: AlbumListReducer): Reducer<AlbumListMutation, AlbumListUiState>

    @Binds
    abstract fun bindGalleryDataSource(galleryDataSourceImpl: GalleryDataSourceImpl): GalleryDataSource

    companion object {
        @Provides
        fun provideGalleryRepository(dataSource: GalleryDataSource): GalleryRepository = GalleryRepository(dataSource)

        @Provides
        fun provideAlbumListActionProcessor(repository: GalleryRepository): AlbumListActionProcessor = AlbumListActionProcessor(repository)
    }
}