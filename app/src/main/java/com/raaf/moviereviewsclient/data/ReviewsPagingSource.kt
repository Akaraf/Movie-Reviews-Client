package com.raaf.moviereviewsclient.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raaf.moviereviewsclient.Paging
import com.raaf.moviereviewsclient.dataModels.Review
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class ReviewsPagingSource @Inject constructor(
    private val dataSource: ReviewsDataSource
) : PagingSource<Int, Review>() {

    var savedPage: Int? = null
    var isNeedToRefillingUIFlow = MutableSharedFlow<Boolean>()
    var isNeedToShowToastFlow = MutableSharedFlow<Boolean>()

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val page = setPage(params.key)
            val reviews = dataSource.getReviews(page)
            updateUIConfiguration()
            if (reviews.isEmpty()) throw Exception("no data")
            val previousKey = if (page != Paging.INITIAL_PAGE) page - 1 else null
            val nextKey = if (dataSource.isLastPage) null else page + 1
            LoadResult.Page(reviews, previousKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private suspend fun updateUIConfiguration() {
        if (dataSource.isNeedToShowToast) isNeedToShowToastFlow.emit(true)
        if (dataSource.isNeedToDestroyDataSource) isNeedToRefillingUIFlow.emit(true)
    }

    private fun setPage(paramsPage: Int?) : Int {
        val result = if (savedPage != null) savedPage!!
                else paramsPage ?: Paging.INITIAL_PAGE
        savedPage = null
        return result
    }
}