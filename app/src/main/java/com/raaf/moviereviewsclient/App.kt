package com.raaf.moviereviewsclient

import android.app.Application
import com.raaf.moviereviewsclient.di.components.DaggerReviewsComponent
import com.raaf.moviereviewsclient.di.components.ReviewsComponent

class
App : Application() {

    companion object {
        lateinit var reviewsComponent: ReviewsComponent
    }

    override fun onCreate() {
        super.onCreate()
        reviewsComponent = DaggerReviewsComponent.builder()
            .application(this)
            .build()
    }
}