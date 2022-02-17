package com.raaf.moviereviewsclient.data.webApi.responseModels

import com.raaf.moviereviewsclient.dataModels.Review

data class ReviewsResponse(
    var results: List<Review>
)