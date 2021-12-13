package com.raaf.moviereviewsclient.data.webApi

import com.raaf.moviereviewsclient.data.webApi.responseModels.ReviewsResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "l1dRWWoZl0venQADYN6rNluGe5pPFvSz"
private const val BASE_URL = "https://api.nytimes.com/svc/movies/v2/"

interface ReviewsService {

    @GET("reviews/{type}.json")
    suspend fun getReviews(@Path("type") type: String,
                   @Query("api-key") key: String = API_KEY,
                   @Query("offset") offset: Int) : ReviewsResponse

    companion object {
        fun create() : ReviewsService {
            val okHttpClient = OkHttpClient.Builder()
                .build()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ReviewsService::class.java)
        }
    }
}