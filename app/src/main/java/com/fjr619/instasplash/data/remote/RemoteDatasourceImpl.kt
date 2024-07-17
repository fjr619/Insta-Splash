package com.fjr619.instasplash.data.remote

import com.fjr619.instasplash.data.remote.dto.UnsplashImageDto
import com.fjr619.instasplash.data.remote.request.FeedImages
import com.fjr619.instasplash.data.remote.request.ImageById
import com.fjr619.instasplash.data.remote.request.SearchImages
import com.fjr619.instasplash.data.remote.response.UnsplashImagesSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import org.koin.core.annotation.Single

@Single
class RemoteDatasourceImpl(
    private val client: HttpClient
): RemoteDatasource {
    override suspend fun getEditorialFeedImages(page: Int, perPage: Int): List<UnsplashImageDto> {
        return client.get(FeedImages(page, perPage)).body()
    }

    override suspend fun searchImages(
        page: Int,
        perPage: Int,
        query: String
    ): UnsplashImagesSearchResponse {
        return client.get(SearchImages(page, perPage, query)).body()
    }

    override suspend fun getImagesById(id: String): UnsplashImageDto {
        return client.get(ImageById.Id(imageId = id)).body()
    }
}