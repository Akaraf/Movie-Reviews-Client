package com.raaf.moviereviewsclient.dataModels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class MultiMedia(
    @SerializedName("type")val imageName: String,
    @SerializedName("src") var url: String?
) : Parcelable