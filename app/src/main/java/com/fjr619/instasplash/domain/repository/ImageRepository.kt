package com.fjr619.instasplash.domain.repository

import com.fjr619.instasplash.domain.model.UnsplashImage

interface ImageRepository {
    suspend fun getEditorialFeedImages(): List<UnsplashImage>
}