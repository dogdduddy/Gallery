package com.pixo.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pixo.gallery.albumList.AlbumListUiState
import com.pixo.gallery.albumList.AlbumListViewModel
import com.pixo.gallery.ui.theme.GalleryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GalleryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AlbumListWithPermission()
                }
            }
        }
    }
}

@Composable
fun AlbumListWithPermission() {

    // 권한 요청 로직
    PermissionRequester(
        permission = Permissions.readExternalStoragePermission,
        onDismissRequest = {},
        onPermissionGranted = {},
        onPermissionDenied = {}
    )

    AlbumListScreen(onAlbumClick = {
        // 앨범 클릭 시 동작 정의
    })
}

@Composable
fun AlbumListScreen(viewModel: AlbumListViewModel = hiltViewModel(), onAlbumClick: (Album) -> Unit) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is AlbumListUiState.Loading -> {
            Text(text = "Loading...")
        }
        is AlbumListUiState.Empty -> {
            Text(text = "No albums found")
        }
        is AlbumListUiState.Content -> {
            val content = state as AlbumListUiState.Content
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                state = rememberLazyGridState()
            ) {
                items(content.albums.size, span = { _ ->
                    GridItemSpan(1)
                }) { index ->
                    val album = content.albums[index]
                    AlbumItem(album = album, onAlbumClick = { onAlbumClick(album) })
                }
            }
        }
        is AlbumListUiState.Error -> {
            Text(text = (state as AlbumListUiState.Error).message)
        }
    }

}

@Composable
fun AlbumItem(album: Album, onAlbumClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onAlbumClick() }
    ) {
        Text(text = album.name, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "${album.photosCount} Images", style = MaterialTheme.typography.bodySmall)
    }
}
