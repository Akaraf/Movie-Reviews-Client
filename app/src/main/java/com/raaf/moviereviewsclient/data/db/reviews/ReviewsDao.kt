package com.raaf.moviereviewsclient.data.db.reviews

import androidx.room.*
import com.raaf.moviereviewsclient.data.db.BaseDao
import com.raaf.moviereviewsclient.dataModels.Review

@Dao
interface ReviewsDao : BaseDao<Review> {

    @Transaction
    @Query("SELECT * FROM reviews")
    suspend fun getAll() : List<Review>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reviews: List<Review>)

    @Query("DELETE FROM reviews")
    suspend fun deleteAll()
}