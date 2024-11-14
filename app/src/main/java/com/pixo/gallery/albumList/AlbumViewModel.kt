package com.pixo.gallery.albumList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pixo.gallery.ActionProcessor
import com.pixo.gallery.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumListViewModel @Inject constructor(
    private val albumListReducer: Reducer<AlbumListMutation, AlbumListUiState>,
    private val actionProcessor: ActionProcessor<AlbumListUiIntent, AlbumListMutation, AlbumListEvent>
) : ViewModel() {

    private val _state =
        MutableStateFlow<AlbumListUiState>(AlbumListUiState.Empty)
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AlbumListEvent>()
    val event = _event.asSharedFlow()

    fun processIntent(intent: AlbumListUiIntent) {
        viewModelScope.launch {
            actionProcessor(intent).collect { value ->
                value.first?.let(::handleMutation)
                value.second?.let {
                    _event.tryEmit(it)
                }
            }
        }
    }

    private fun handleMutation(mutation: AlbumListMutation) {
        _state.update {
            albumListReducer(mutation, _state.value)
        }
    }
}