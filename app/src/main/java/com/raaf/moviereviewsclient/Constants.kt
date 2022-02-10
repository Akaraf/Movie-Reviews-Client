package com.raaf.moviereviewsclient

object Paging {
    const val INITIAL_PAGE = 0
    const val PAGE_SIZE = 20
    const val INITIAL_LOAD_SIZE = 60
}

object Review {
    const val PARCELABLE_REVIEW = "ParcelableReview"
}

object Databases {
    const val REVIEWS_DATABASE_NAME = "reviewsDatabase"
    const val REVIEWS_TABLE_NAME = "reviews"
    const val REVIEWS_COUNT_CACHED_PAGE = 3
}