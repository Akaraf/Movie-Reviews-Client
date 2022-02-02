package com.raaf.moviereviewsclient.dataModels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Link(
    var type: String?,
    @SerializedName("url") var reviewUrl: String?,
    @SerializedName("suggested_link_text") var suggest: String?,
) : Parcelable