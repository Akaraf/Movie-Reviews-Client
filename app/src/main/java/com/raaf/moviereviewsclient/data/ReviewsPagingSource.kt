package com.raaf.moviereviewsclient.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.raaf.moviereviewsclient.dataModels.Review
import javax.inject.Inject

private const val TYPE_ALL = "all"

class ReviewsPagingSource @Inject constructor(
    private val repository: ReviewsRepository  // We can using web service instead this
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
            val pageSize = 20
            val reviews = repository.getReviews(TYPE_ALL, page * 20)
            val previousKey = if (page != 0) page - 1 else null
            val nextKey = if (reviews.count() < pageSize) null else page + 1
            return LoadResult.Page(reviews, previousKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    private fun setPage(paramsPage: Int?) : Int {
        val result = if (savedPage != null) savedPage!!
                else paramsPage ?: 0
        savedPage = null
        return result
    }
}