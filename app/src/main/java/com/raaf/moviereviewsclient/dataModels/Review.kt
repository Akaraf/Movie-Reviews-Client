package com.raaf.moviereviewsclient.dataModels

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("display_title") var filmName: String,
    @SerializedName("summary_short") var reviewContent: String,
    var multimedia: MultiMedia
)