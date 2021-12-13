package com.raaf.moviereviewsclient.di.modules

import com.raaf.moviereviewsclient.data.webApi.ReviewsService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ReviewsServiceModule {

    @Provides
    @Singleton
    fun provideWebService() : ReviewsService {
        return ReviewsService.create()
    }
}