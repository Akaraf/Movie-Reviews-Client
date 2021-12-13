package com.raaf.moviereviewsclient.data.webApi.responseModels

import com.raaf.moviereviewsclient.dataModels.Review

class ReviewsResponse {
    lateinit var results: List<Review>
}