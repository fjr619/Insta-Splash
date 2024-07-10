package com.fjr619.instasplash.presentation.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjr619.instasplash.data.mapper.toDomainModelList
import com.fjr619.instasplash.data.remote.RemoteDatasource
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val imageRepository: ImageRepository,
): ViewModel() {

    var images: List<UnsplashImage> by mutableStateOf(emptyList())
        private set


    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            try {
                images = imageRepository.getEditorialFeedImages()
            } catch (e: Exception) {
                println("error ${e.message}")
            }
        }
    }

}