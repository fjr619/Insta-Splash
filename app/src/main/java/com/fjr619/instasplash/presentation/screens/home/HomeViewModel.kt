package com.fjr619.instasplash.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository
import com.fjr619.instasplash.presentation.screens.favorite.FavoritesAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeState(
    val images: Flow<PagingData<UnsplashImage>> = flow { PagingData.empty<UnsplashImage>() },
    val favoritesImageIds: List<String> = listOf()
)

sealed interface HomeAction {
    data class ToggleFavoriteStatus(val image: UnsplashImage) : HomeAction
}

class HomeViewModel(
    private val imageRepository: ImageRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = combine(
        _state,
        imageRepository.getEditorialFeedImages().cachedIn(viewModelScope),
        imageRepository.getFavoriteImageIds()
    ) { state, images, imagesIds ->
        state.copy(
            images = flow { emit(images) },
            favoritesImageIds = imagesIds
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = HomeState()
    )

    fun onAction(action: HomeAction) {
        when(action) {
            is HomeAction.ToggleFavoriteStatus -> {
                toggleFavoriteStatus(action.image)
            }
        }
    }

    private fun toggleFavoriteStatus(image: UnsplashImage) {
        viewModelScope.launch {
            try {
                imageRepository.toggleFavoriteStatus(image)
            } catch (e: Exception) {
                //todo
            }
        }
    }

}