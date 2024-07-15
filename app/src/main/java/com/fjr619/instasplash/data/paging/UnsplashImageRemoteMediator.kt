package com.fjr619.instasplash.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.fjr619.instasplash.data.local.ImageSplashDatabase
import com.fjr619.instasplash.data.local.entities.UnsplashImageDao
import com.fjr619.instasplash.data.local.entities.UnsplashImageEntity
import com.fjr619.instasplash.data.local.entities.UnsplashRemoteKeys
import com.fjr619.instasplash.data.mapper.toEntityList
import com.fjr619.instasplash.data.remote.RemoteDatasource
import com.fjr619.instasplash.data.util.Constants
import com.fjr619.instasplash.data.util.Constants.ITEMS_PER_PAGE
import com.fjr619.instasplash.data.util.Constants.ITEMS_PER_PAGE_FROM_DB

@OptIn(ExperimentalPagingApi::class)
class UnsplashImageRemoteMediator(
    private val remoteDatasource: RemoteDatasource,
    private val database: ImageSplashDatabase,
    private val unsplashImageDao: UnsplashImageDao
) : RemoteMediator<Int, UnsplashImageEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UnsplashImageEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: Constants.STARTING_PAGE_INDEX
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKeys != null
                        )
                    nextPage
                }
            }

            val response =
                remoteDatasource.getEditorialFeedImages(
                    page = currentPage,
                    perPage = ITEMS_PER_PAGE_FROM_DB
                )

            val endOfPaginationReached = response.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    unsplashImageDao.deleteAllEditorialFeedImages()
                    unsplashImageDao.deleteAllRemoteKeys()
                }
                val remoteKeys = response.map { unsplashImageDto ->
                    UnsplashRemoteKeys(
                        id = unsplashImageDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }
                unsplashImageDao.insertAllRemoteKeys(remoteKeys)
                unsplashImageDao.insertEditorialFeedImages(response.toEntityList())
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, UnsplashImageEntity>
    ): UnsplashRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                unsplashImageDao.getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, UnsplashImageEntity>
    ): UnsplashRemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { unsplashImage ->
                unsplashImageDao.getRemoteKeys(id = unsplashImage.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, UnsplashImageEntity>
    ): UnsplashRemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { unsplashImage ->
                unsplashImageDao.getRemoteKeys(id = unsplashImage.id)
            }
    }
}