package com.fjr619.instasplash.data.repository

import com.fjr619.instasplash.data.mapper.toDomainModelList
import com.fjr619.instasplash.data.remote.RemoteDatasource
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository

class ImageRepositoryImpl(
    private val remoteDatasource: RemoteDatasource
): ImageRepository {
    override suspend fun getEditorialFeedImages(): List<UnsplashImage> {
        return remoteDatasource.getEditorialFeedImages(
            page = 1,
            perPage = 10
        ).toDomainModelList()
    }
}