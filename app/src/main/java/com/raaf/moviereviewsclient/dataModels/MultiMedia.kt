package com.raaf.moviereviewsclient.dataModels

import com.google.gson.annotations.SerializedName

data class MultiMedia(
    @SerializedName("type")val imageName: String,
    @SerializedName("src") var url: String?
)