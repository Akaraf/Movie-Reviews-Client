package com.raaf.moviereviewsclient.data

import android.util.Log
import com.raaf.moviereviewsclient.Databases.REVIEWS_COUNT_CACHED_PAGE
import com.raaf.moviereviewsclient.Paging.INITIAL_PAGE
import com.raaf.moviereviewsclient.Paging.PAGE_SIZE
import com.raaf.moviereviewsclient.data.db.reviews.ReviewsDao
import com.raaf.moviereviewsclient.data.db.reviews.ReviewsDatabase
import com.raaf.moviereviewsclient.data.webApi.ReviewsService
import com.raaf.moviereviewsclient.dataModels.Review
import kotlinx.coroutines.*
import java.lang.IndexOutOfBoundsException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ReviewsDataSource @Inject constructor(
    private val apiService: ReviewsService,
    private val reviewsDB: ReviewsDatabase) {

    companion object {
        private const val TAG = "ReviewsDataSource"
    }

//    only for api
    var isLastPage = false
    private set
    private var isUsingDBSource = false
    var isNeedToShowToast = false
    private set
    var isNeedToDestroyDataSource = false
    private set

    val reviewsDao: ReviewsDao by lazy {
        reviewsDB.reviewsDao()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler {
            coroutineContext, throwable ->
        Log.e(TAG, "Exception: ${throwable.message}")
    }
    private val defaultCC: CoroutineContext = Dispatchers.Default +
            coroutineExceptionHandler + SupervisorJob()
    private val IOCC = Dispatchers.IO +
            coroutineExceptionHandler + SupervisorJob()
    private val coroutineScope: CoroutineScope = CoroutineScope(defaultCC)

    private var reviewsFromDB: List<Review> = listOf()

    suspend fun getReviews(page: Int) : List<Review> {
        resetPreviousConfiguration()
        val reviewsFromApi = getReviewsFromApi(page)
        val resultReviews: List<Review>
        when {
//            take data from web
            reviewsFromApi.isNotEmpty() && !isUsingDBSource -> {
                if (page in INITIAL_PAGE until REVIEWS_COUNT_CACHED_PAGE) {
                    saveReviewsInDB(reviewsFromApi, page)
                }
                if (reviewsFromApi.count() in 1 until PAGE_SIZE) isLastPage = true
                resultReviews = reviewsFromApi
            }
//            starting online mode and take data from web
            reviewsFromApi.isNotEmpty() && isUsingDBSource -> {
                startOnlineMod()
                resultReviews = listOf()
            }
//            starting offline mode and take data from cache
            reviewsFromApi.isEmpty() && page == INITIAL_PAGE -> {
                startOfflineMod()
                resultReviews = getReviewsFromDB(page)
            }
//            take data from cache
            reviewsFromApi.isEmpty() && isUsingDBSource
                    && page in INITIAL_PAGE until REVIEWS_COUNT_CACHED_PAGE -> {
                resultReviews = getReviewsFromDB(page)
            }
//            no data
            else -> {
                resultReviews = listOf<Review>()
            }
        }
        return resultReviews
    }

    private fun startOnlineMod() {
        isUsingDBSource = false
        isNeedToDestroyDataSource = true
    }

    private fun startOfflineMod() {
        isNeedToShowToast = true
        isUsingDBSource = true
    }

    private fun resetPreviousConfiguration() {
        isNeedToDestroyDataSource = false
        isNeedToShowToast = false
    }

    private suspend fun loadReviewsFromDB() {
        coroutineScope.launch {
            reviewsFromDB = reviewsDao.getAll()
        }.join()
    }

    private suspend fun getReviewsFromDB(page: Int) : List<Review> {
        if (reviewsFromDB.isEmpty()) loadReviewsFromDB()
        return try {
            reviewsFromDB.subList(page * PAGE_SIZE, (page + 1) * PAGE_SIZE)
        } catch (e: IndexOutOfBoundsException) {
            if (reviewsFromDB.lastIndex >= page * PAGE_SIZE) {
                reviewsFromDB.subList(page * PAGE_SIZE, reviewsFromDB.lastIndex)
            } else emptyList<Review>()
        }
    }

    private suspend fun saveReviewsInDB(reviews: List<Review>, page: Int) {
        if (page == INITIAL_PAGE) {
            coroutineScope.launch {
                reviewsDB.clearAllTables()
                reviewsDao.clearTableOptions()
            }
        }
        coroutineScope.launch {
            reviewsDao.insert(reviews)
        }
    }

    private suspend fun getReviewsFromApi(page: Int) : List<Review> {
        var resultRequest = listOf<Review>()
        coroutineScope.launch(IOCC) {
            resultRequest = apiService.getReviews(
                ReviewsService.Options.TYPE_ALL,
                offset = page * PAGE_SIZE
            ).results
        }.join()
        return resultRequest
    }
}