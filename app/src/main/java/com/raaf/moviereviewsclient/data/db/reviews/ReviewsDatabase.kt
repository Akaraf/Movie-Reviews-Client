package com.raaf.moviereviewsclient.data.db.reviews

import androidx.room.Database
import androidx.room.RoomDatabase
import com.raaf.moviereviewsclient.dataModels.Review

@Database(entities = arrayOf(Review::class), version = 1, exportSchema = false)
abstract class ReviewsDatabase : RoomDatabase() {

    abstract fun reviewsDao() : ReviewsDao
}