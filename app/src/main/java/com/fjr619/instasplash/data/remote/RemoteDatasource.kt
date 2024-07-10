package com.fjr619.instasplash.data.remote

import com.fjr619.instasplash.data.remote.dto.UnsplashImageDto
import com.fjr619.instasplash.data.remote.response.UnsplashImagesSearchResponse

interface RemoteDatasource {
    suspend fun getEditorialFeedImages(page: Int, perPage: Int): List<UnsplashImageDto>
    suspend fun searchImages(page: Int, perPage: Int, query: String): UnsplashImagesSearchResponse
    suspend fun getImagesById(id: String): UnsplashImageDto
}