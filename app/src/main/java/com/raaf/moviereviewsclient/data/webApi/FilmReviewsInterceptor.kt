package com.raaf.moviereviewsclient.data.webApi

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "Interceptor"

class FilmReviewsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val overrideRequest = originalRequest.newBuilder()
            .url(originalRequest.url())
            .build()

        Log.e(TAG, originalRequest.url().toString())

        return chain.proceed(overrideRequest)
    }
}