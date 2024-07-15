package com.fjr619.instasplash.presentation.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fjr619.instasplash.di.viewModelModule
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoriteState(
    val images: Flow<PagingData<UnsplashImage>> = flow { PagingData.empty<UnsplashImage>() },
    val favoritesImageIds: List<String> = listOf()
)

sealed interface FavoritesAction {
    data class ToggleFavoriteStatus(val image: UnsplashImage): FavoritesAction
}

class FavoritesViewModel(
    private val repository: ImageRepository
): ViewModel() {

    private val _state = MutableStateFlow(FavoriteState())
    val state = combine(
        _state,
        repository.getAllFavoriteImages(),
        repository.getFavoriteImageIds()
    ) { state, images, imagesIds ->
        state.copy(
            images = flow { emit(images) }.cachedIn(viewModelScope),
            favoritesImageIds = imagesIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = FavoriteState()
    )

    fun onAction(action: FavoritesAction) {
        when(action) {
            is FavoritesAction.ToggleFavoriteStatus -> {
                toggleFavoriteStatus(action.image)
            }
        }
    }

    private fun toggleFavoriteStatus(image: UnsplashImage) {
        viewModelScope.launch {
            try {
                repository.toggleFavoriteStatus(image)
            } catch (e: Exception) {
                //todo
            }
        }
    }
}