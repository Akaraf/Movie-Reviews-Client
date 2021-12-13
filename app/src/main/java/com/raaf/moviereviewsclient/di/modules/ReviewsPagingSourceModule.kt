package com.raaf.moviereviewsclient.di.modules

import com.raaf.moviereviewsclient.data.ReviewsPagingSource
import com.raaf.moviereviewsclient.data.ReviewsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = arrayOf(ReviewsRepositoryModule::class))
class ReviewsPagingSourceModule {

    @Provides
    @Singleton
    fun providePagingSource(repository: ReviewsRepository) : ReviewsPagingSource {
        return ReviewsPagingSource(repository)
    }
}