package com.raaf.moviereviewsclient.data.db

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: T)

    @Delete
    fun delete(obj: T)
}