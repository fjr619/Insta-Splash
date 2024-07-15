package com.fjr619.instasplash.domain.repository

import androidx.paging.PagingData
import com.fjr619.instasplash.domain.model.Response
import com.fjr619.instasplash.domain.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getEditorialFeedImages(): List<UnsplashImage>
    fun getImage(imageId: String): Flow<Response<UnsplashImage>>
    fun searchImages(query: String): Flow<PagingData<UnsplashImage>>

    fun getAllFavoriteImages(): Flow<PagingData<UnsplashImage>>
    suspend fun toggleFavoriteStatus(image: UnsplashImage)
    fun getFavoriteImageIds(): Flow<List<String>>
}