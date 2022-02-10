package com.raaf.moviereviewsclient.di.modules

import com.raaf.moviereviewsclient.data.ReviewsDataSource
import com.raaf.moviereviewsclient.data.ReviewsPagingSource
import com.raaf.moviereviewsclient.data.ReviewsPagingSourceProvider
import com.raaf.moviereviewsclient.data.ReviewsRepository
import com.raaf.moviereviewsclient.data.db.reviews.ReviewsDatabase
import com.raaf.moviereviewsclient.data.webApi.ReviewsService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(
    ReviewsServiceModule::class,
    ReviewsDatabaseModule::class))
class ReviewsPagingSourceModule {

    @Provides
    fun providePagingSourceProvider() : ReviewsPagingSourceProvider {
        return ReviewsPagingSourceProvider()
    }

    @Provides
    fun provideReviewsDataSource(webService: ReviewsService,
                                 reviewsDatabase: ReviewsDatabase) : ReviewsDataSource {
        return ReviewsDataSource(webService, reviewsDatabase)
    }

    @Provides
    fun providePagingSource(dataSource: ReviewsDataSource) : ReviewsPagingSource {
        return ReviewsPagingSource(dataSource)
    }
}