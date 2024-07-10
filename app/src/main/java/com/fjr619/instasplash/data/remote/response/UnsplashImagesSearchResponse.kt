package com.fjr619.instasplash.data.remote.response

import com.fjr619.instasplash.data.remote.dto.UnsplashImageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImagesSearchResponse(
    @SerialName("results")
    val images: List<UnsplashImageDto>,
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int
)