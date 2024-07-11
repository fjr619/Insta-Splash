package com.fjr619.instasplash.presentation.screens.full_image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.instasplash.domain.model.Response
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository
import com.fjr619.instasplash.presentation.screens.navArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class FullImageState(
    var isLoading: Boolean = true,
    var isError: Boolean = false,
    val image: UnsplashImage? = null
)

class FullImageViewModel(
    private val repository: ImageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: FullImageScreenNavArgs = savedStateHandle.navArgs()

    private val _state = MutableStateFlow(FullImageState())
    val state = repository.getImage(navArgs.imageId).map { image ->
        println("test $image")
        when (image) {
            is Response.Success -> {
                FullImageState(
                    isLoading = false,
                    isError = false,
                    image = image.data
                )
            }
            is Response.Error -> {
                FullImageState(
                    isLoading = false,
                    isError = true,
                    image = null,
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = FullImageState()
    )

    fun updateLoading(value: Boolean) {
        _state.update {
            it.copy(
                isLoading = value
            )
        }
    }

    fun updateError(value: Boolean) {
        _state.update {
            it.copy(
                isError = value
            )
        }
    }
}