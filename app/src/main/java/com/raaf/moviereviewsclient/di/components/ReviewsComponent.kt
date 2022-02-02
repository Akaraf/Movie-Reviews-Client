package com.raaf.moviereviewsclient.di.components

import android.app.Application
import com.raaf.moviereviewsclient.di.modules.ReviewsPagingSourceModule
import com.raaf.moviereviewsclient.di.modules.ReviewsServiceModule
import com.raaf.moviereviewsclient.ui.DetailReviewViewModel
import com.raaf.moviereviewsclient.ui.ReviewsViewModel
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = arrayOf(ReviewsPagingSourceModule::class, ReviewsServiceModule::class))
@Singleton
interface ReviewsComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ReviewsComponent
    }

    fun reviewsViewModel() : ReviewsViewModel.Factory
    fun detailReviewViewModel() : DetailReviewViewModel.Factory
}