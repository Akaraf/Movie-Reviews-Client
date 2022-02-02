package com.raaf.moviereviewsclient.dataModels

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Review(
    @SerializedName("display_title") var filmName: String,
    @SerializedName("summary_short") var reviewContent: String,
    @SerializedName("mpaa_rating") var mpaaRating: String?,
    @SerializedName("byline") var authorName: String?,
    var headline: String?,
    @SerializedName("publication_date") var reviewPublicationDate: String?,
    @SerializedName("date_updated") var reviewLastUpdateDate: String?,
    @SerializedName("opening_date") var movieOpeningDate: String?,
    var link: Link,
    var multimedia: MultiMedia
) : Parcelable