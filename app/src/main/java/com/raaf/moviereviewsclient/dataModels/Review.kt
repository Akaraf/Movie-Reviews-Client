package com.raaf.moviereviewsclient.dataModels

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.raaf.moviereviewsclient.Databases.REVIEWS_TABLE_NAME
import com.raaf.moviereviewsclient.dataModels.typeConverters.LinkTypeConverter
import com.raaf.moviereviewsclient.dataModels.typeConverters.MultiMediaTypeConverter
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = REVIEWS_TABLE_NAME)
@TypeConverters(LinkTypeConverter::class, MultiMediaTypeConverter::class)
data class Review(
    @PrimaryKey(autoGenerate = true) var DBId: Int = 0,
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