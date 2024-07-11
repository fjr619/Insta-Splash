package com.fjr619.instasplash.domain.repository

import com.fjr619.instasplash.domain.model.Response
import com.fjr619.instasplash.domain.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getEditorialFeedImages(): List<UnsplashImage>
    fun getImage(imageId: String): Flow<Response<UnsplashImage>>
}