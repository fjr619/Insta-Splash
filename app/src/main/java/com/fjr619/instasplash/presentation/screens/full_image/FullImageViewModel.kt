package com.fjr619.instasplash.presentation.screens.full_image

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.instasplash.domain.model.Response
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageDownloaderRepository
import com.fjr619.instasplash.domain.repository.ImageRepository
import com.fjr619.instasplash.presentation.screens.navArgs
import com.fjr619.instasplash.presentation.util.snackbar.AppSnackbarVisual
import com.fjr619.instasplash.presentation.util.snackbar.SnackbarMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FullImageState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val image: UnsplashImage? = null,
    val snackbarMessage: SnackbarMessage? = null
)

class FullImageViewModel(
    private val repository: ImageRepository,
    private val downloader: ImageDownloaderRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: FullImageScreenNavArgs = savedStateHandle.navArgs()

    private val _state = MutableStateFlow(FullImageState())
    val state =
        combine(
            _state,
            repository.getImage(navArgs.imageId)
        ) { state, response ->
            when (response) {
                is Response.Success -> {
                    state.copy(
                        isLoading = false,
                        isError = false,
                        image = response.data,
                        snackbarMessage = null
                    )
                }

                is Response.Error -> {
                    state.copy(
                        isLoading = false,
                        isError = true,
                        image = null,
                        snackbarMessage = SnackbarMessage.from(
                            snackbarVisuals = AppSnackbarVisual(
                                message = response.message ?: "ERROR"
                            ),
                            onSnackbarResult = {}
                        )
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = FullImageState()
        )

    fun dismissSnackbar() = _state.update { it.copy(snackbarMessage = null) }

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

    fun downloadImage(url: String, title: String?) {
        viewModelScope.launch {
            try {
                downloader.downloadFile(url, title)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}