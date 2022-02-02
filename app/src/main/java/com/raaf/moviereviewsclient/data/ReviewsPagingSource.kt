package com.raaf.moviereviewsclient.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raaf.moviereviewsclient.Paging
import com.raaf.moviereviewsclient.data.webApi.ReviewsService
import com.raaf.moviereviewsclient.data.webApi.ReviewsService.Options.TYPE_ALL
import com.raaf.moviereviewsclient.dataModels.Review
import javax.inject.Inject

class ReviewsPagingSource @Inject constructor(
    private val apiService: ReviewsService
) : PagingSource<Int, Review>() {

    var savedPage: Int? = null

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        try {
            val page = setPage(params.key)
            val reviews = apiService.getReviews(
                TYPE_ALL,
                offset = page * Paging.PAGE_SIZE
            ).results
            val previousKey = if (page != Paging.INITIAL_PAGE) page - 1 else null
            val nextKey = if (reviews.count() < Paging.PAGE_SIZE) null else page + 1
            return LoadResult.Page(reviews, previousKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun setPage(paramsPage: Int?) : Int {
        val result = if (savedPage != null) savedPage!!
                else paramsPage ?: Paging.INITIAL_PAGE
        savedPage = null
        return result
    }
}