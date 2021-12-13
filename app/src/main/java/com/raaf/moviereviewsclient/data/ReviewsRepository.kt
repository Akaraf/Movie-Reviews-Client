package com.raaf.moviereviewsclient.data

import android.util.Log
import com.raaf.moviereviewsclient.data.webApi.ReviewsService
import com.raaf.moviereviewsclient.dataModels.Review

private const val TAG = "FilmReviewsRepository"

class ReviewsRepository(
    private val filmReviewsService: ReviewsService
) {

    suspend fun getReviews(type: String, offset: Int) : List<Review> {
        val response = filmReviewsService.getReviews(type, offset = offset)
        return response.results
    }
}