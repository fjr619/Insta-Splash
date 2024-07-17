package com.fjr619.instasplash.presentation.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

data class FavoriteState(
    val favoritesImageIds: List<String> = listOf()
)

sealed interface FavoritesAction {
    data class ToggleFavoriteStatus(val image: UnsplashImage): FavoritesAction
}

@KoinViewModel
class FavoritesViewModel(
    private val repository: ImageRepository
): ViewModel() {

    val images: Flow<PagingData<UnsplashImage>> = repository.getAllFavoriteImages().cachedIn(viewModelScope)

    private val _state = MutableStateFlow(FavoriteState())
    val state = combine(
        _state,
        repository.getFavoriteImageIds()
    ) { state, imagesIds ->
        state.copy(
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