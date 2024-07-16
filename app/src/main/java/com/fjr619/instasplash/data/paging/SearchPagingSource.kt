package com.fjr619.instasplash.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fjr619.instasplash.data.mapper.toDomainModelList
import com.fjr619.instasplash.data.remote.RemoteDatasource
import com.fjr619.instasplash.data.util.Constants
import com.fjr619.instasplash.data.util.Constants.STARTING_PAGE_INDEX
import com.fjr619.instasplash.domain.model.UnsplashImage

class SearchPagingSource(
    private val query: String,
    private val remoteDatasource: RemoteDatasource
): PagingSource<Int, UnsplashImage>() {

    override fun getRefreshKey(state: PagingState<Int, UnsplashImage>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImage> {
        val currentPage = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = remoteDatasource.searchImages(
                query = query,
                page = currentPage,
                perPage = params.loadSize
            )
            val endOfPaginationReached = response.images.isEmpty()

            LoadResult.Page(
                data = response.images.toDomainModelList(),
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}