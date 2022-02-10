package com.raaf.moviereviewsclient.di.modules

import android.app.Application
import androidx.room.Room
import com.raaf.moviereviewsclient.Databases.REVIEWS_DATABASE_NAME
import com.raaf.moviereviewsclient.data.db.reviews.ReviewsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReviewsDatabaseModule {

    @Provides
    @Singleton
    fun provideReviewsDatabase(app: Application) : ReviewsDatabase {
        return Room.databaseBuilder(
            app.applicationContext,
            ReviewsDatabase::class.java,
            REVIEWS_DATABASE_NAME).build()
    }
}