package com.fjr619.instasplash.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class SearchState(
    val searchQuery: String = "",
    val images: Flow<PagingData<UnsplashImage>> = flow { PagingData.empty<UnsplashImage>() },
    val favoritesImageIds: List<String> = listOf()
)

sealed interface SearchAction {
    data class SearchQueryChanged(val query: String): SearchAction
    data object Search: SearchAction
    data class ToggleFavoriteStatus(val image: UnsplashImage): SearchAction
}

sealed interface SearchEvent {
    data object DoScrollUp: SearchEvent
}

@KoinViewModel
class SearchViewModel(
    private val repository: ImageRepository
): ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private val eventChannel = Channel<SearchEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.getFavoriteImageIds()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { data ->
                    _searchState.update {
                        it.copy(
                            favoritesImageIds = data
                        )
                    }
                }
        }

    }

    fun onAction(action: SearchAction) {
        when(action) {
            is SearchAction.SearchQueryChanged -> {
                _searchState.update {
                    it.copy(searchQuery = action.query)
                }

                if (action.query.isEmpty()) {
                    _searchState.update {
                        it.copy(
                            images = flow { emit(PagingData.empty()) }
                        )
                    }
                }
            }
            is SearchAction.Search -> {
                searchImages()
            }
            is SearchAction.ToggleFavoriteStatus -> {
                toggleFavoriteStatus(action.image)
            }
        }
    }

    private fun searchImages() {
        viewModelScope.launch {
            try {
                val images = repository.searchImages(searchState.value.searchQuery)
                    .catch { e ->
                        e.printStackTrace()
                    }
                    .cachedIn(viewModelScope)
                _searchState.update {
                    it.copy(images = images)
                }
                eventChannel.send(SearchEvent.DoScrollUp)
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun toggleFavoriteStatus(image: UnsplashImage) {
        viewModelScope.launch {
            try {
                repository.toggleFavoriteStatus(image)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}