package com.raaf.moviereviewsclient.di.modules

import com.raaf.moviereviewsclient.data.ReviewsPagingSource
import com.raaf.moviereviewsclient.data.ReviewsRepository
import com.raaf.moviereviewsclient.data.webApi.ReviewsService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ReviewsServiceModule::class))
class ReviewsPagingSourceModule {

    @Provides
    @Singleton
    fun providePagingSource(webService: ReviewsService) : ReviewsPagingSource {
        return ReviewsPagingSource(webService)
    }
}