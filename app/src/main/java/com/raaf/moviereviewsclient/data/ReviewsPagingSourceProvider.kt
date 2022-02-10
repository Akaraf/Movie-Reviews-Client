package com.raaf.moviereviewsclient.data

import com.raaf.moviereviewsclient.App
import javax.inject.Inject

class ReviewsPagingSourceProvider {

    @Inject lateinit var pagingSource: ReviewsPagingSource

    private fun injectPagingSource() {
        App.reviewsComponent.inject(this)
    }

    fun providePagingSource() : ReviewsPagingSource {
        injectPagingSource()
        return pagingSource
    }
}