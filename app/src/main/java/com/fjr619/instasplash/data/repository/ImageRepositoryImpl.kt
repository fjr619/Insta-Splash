package com.fjr619.instasplash.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.fjr619.instasplash.data.local.FavoriteImagesDao
import com.fjr619.instasplash.data.local.ImageSplashDatabase
import com.fjr619.instasplash.data.local.entities.UnsplashImageDao
import com.fjr619.instasplash.data.mapper.toDomainModel
import com.fjr619.instasplash.data.mapper.toFavoriteImageEntity
import com.fjr619.instasplash.data.paging.SearchPagingSource
import com.fjr619.instasplash.data.paging.UnsplashImageRemoteMediator
import com.fjr619.instasplash.data.remote.RemoteDatasource
import com.fjr619.instasplash.data.remote.response.FailedResponseException
import com.fjr619.instasplash.data.util.Constants
import com.fjr619.instasplash.domain.model.Response
import com.fjr619.instasplash.domain.model.UnsplashImage
import com.fjr619.instasplash.domain.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class ImageRepositoryImpl(
    private val remoteDatasource: RemoteDatasource,
    private val database: ImageSplashDatabase,
    private val unsplashImageDao: UnsplashImageDao,
    private val favoriteImagesDao: FavoriteImagesDao,
) : ImageRepository {
    @OptIn(ExperimentalPagingApi::class)
    override fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.ITEMS_PER_PAGE_FROM_DB),
            remoteMediator = UnsplashImageRemoteMediator(
                remoteDatasource,
                database,
                unsplashImageDao
            ),
            pagingSourceFactory = {
                unsplashImageDao.getAllEditorialFeedImages()
            }
        ).flow
            .map { pagingData ->
                pagingData.map {
                    it.toDomainModel()
                }
            }
    }

    override fun getImage(imageId: String): Flow<Response<UnsplashImage>> {
        return flow {
            try {
                val response = remoteDatasource.getImagesById(imageId)
                emit(Response.Success(response.toDomainModel()))
            } catch (e: FailedResponseException) {
                emit(Response.Error(e.message))
            }
        }
    }

    override fun searchImages(query: String): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.ITEMS_PER_PAGE),
            pagingSourceFactory = {
                SearchPagingSource(query = query, remoteDatasource = remoteDatasource)
            }
        ).flow
    }

    override fun getAllFavoriteImages(): Flow<PagingData<UnsplashImage>> {
        return Pager(
            config = PagingConfig(pageSize = Constants.ITEMS_PER_PAGE_FROM_DB),
            pagingSourceFactory = {
                favoriteImagesDao.getAllFavoriteImages()
            }
        ).flow.map { pagingData ->
            pagingData.map {
                it.toDomainModel()
            }
        }
    }

    override suspend fun toggleFavoriteStatus(image: UnsplashImage) {
        val isFavorite = favoriteImagesDao.isImageFavorite(image.id)
        val favoriteImage = image.toFavoriteImageEntity()
        if (isFavorite) {
            favoriteImagesDao.deleteFavoriteImage(favoriteImage)
        } else {
            favoriteImagesDao.insertFavoriteImage(favoriteImage)
        }
    }

    override fun getFavoriteImageIds(): Flow<List<String>> {
        return favoriteImagesDao.getFavoriteImageIds()
    }
}