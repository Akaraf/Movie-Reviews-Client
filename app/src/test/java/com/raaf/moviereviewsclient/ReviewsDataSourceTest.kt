package com.raaf.moviereviewsclient

import com.raaf.moviereviewsclient.data.ReviewsDataSource
import com.raaf.moviereviewsclient.data.db.reviews.ReviewsDao
import com.raaf.moviereviewsclient.data.db.reviews.ReviewsDatabase
import com.raaf.moviereviewsclient.data.webApi.ReviewsService
import com.raaf.moviereviewsclient.data.webApi.responseModels.ReviewsResponse
import com.raaf.moviereviewsclient.dataModels.Link
import com.raaf.moviereviewsclient.dataModels.MultiMedia
import com.raaf.moviereviewsclient.dataModels.Review
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

/*
    We may using runTest instead of runBlocking. This will work as well
    but allow to skip delays inside coroutine.
 */

class ReviewsDataSourceTest {

    var mockedApiReviews: List<Review>
    var mockedDbReviews: List<Review>

    init {
        mockedApiReviews = createMockedReviewsList(20)
        mockedDbReviews = createMockedReviewsList(60)
    }

    private fun createMockedReviewsList(itemsCount: Int) : List<Review> {
        var resultList = mutableListOf<Review>()
        for (count in 0 until itemsCount) {
            resultList.add(
                Review(itemsCount * count, "", "", "", "",
                    "", "", "",
                    "", Link(null,null,null),
                    MultiMedia("", null)
                )
            )
        }
        return resultList
    }

    private fun mockedApiServiceWithoutData() : ReviewsService {
        val mockedApiService =  mockk<ReviewsService>()
        coEvery { mockedApiService.getReviews(
            ReviewsService.Options.TYPE_ALL,
            offset = any()
        ) } returns ReviewsResponse(emptyList<Review>())
        return mockedApiService
    }

    private fun mockedApiServiceWithData() : ReviewsService {
        val mockedApiService =  mockk<ReviewsService>()
        coEvery { mockedApiService.getReviews(
            ReviewsService.Options.TYPE_ALL,
            offset = more(-1)
        ) } returns ReviewsResponse(mockedApiReviews)
        return mockedApiService
    }

    private fun mockedDaoWithData() : ReviewsDao {
        val mockedDao = mockk<ReviewsDao>()
        coEvery { mockedDao.getAll() } returns mockedDbReviews
        coEvery { mockedDao.insert(mockedApiReviews) } returns Unit
        return mockedDao
    }

    private fun mockedDaoWithoutData() : ReviewsDao {
        val mockedDao = mockk<ReviewsDao>()
        coEvery { mockedDao.getAll() } returns listOf<Review>()
        coEvery { mockedDao.insert(mockedApiReviews) } returns Unit
        return mockedDao
    }

    private fun mockedDatabase(mockedDao: ReviewsDao) : ReviewsDatabase {
        val mockedDB = mockk<ReviewsDatabase>()
        every { mockedDB.reviewsDao() } returns mockedDao
        return mockedDB
    }

    @ExperimentalCoroutinesApi
    @Test
    fun takeDataFromWeb() = runTest {
        val mockedDataSource = ReviewsDataSource(
            mockedApiServiceWithData(),
            mockedDatabase(mockedDaoWithData())
        )

        assertEquals(mockedApiReviews, mockedDataSource.getReviews(0))
        assertEquals(mockedApiReviews, mockedDataSource.getReviews(1))
        assertEquals(mockedApiReviews, mockedDataSource.getReviews(10))
        assertEquals(mockedApiReviews, mockedDataSource.getReviews(100))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun startOnlineMod() = runTest {
        val mockedService = mockedApiServiceWithoutData()
        val mockedDataSource = ReviewsDataSource(
            mockedService,
            mockedDatabase(mockedDaoWithData())
        )

        mockedDataSource.getReviews(0)
        assertEquals(true, mockedDataSource.isNeedToShowToast)

        coEvery { mockedService.getReviews(
            ReviewsService.Options.TYPE_ALL,
            offset = more(-1)
        ) } returns ReviewsResponse(mockedApiReviews)

        assertEquals(listOf<Review>(), mockedDataSource.getReviews(1))
        assertEquals(true, mockedDataSource.isNeedToDestroyDataSource)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun startOfflineMode() = runTest {
        val mockedDataSource = ReviewsDataSource(
            mockedApiServiceWithoutData(),
            mockedDatabase(mockedDaoWithData())
        )

        assertEquals(mockedDbReviews.subList(0, 20), mockedDataSource.getReviews(0))
        assertEquals(true, mockedDataSource.isNeedToShowToast)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun takeDataFromCache() = runTest {
        val mockedDataSource = ReviewsDataSource(
            mockedApiServiceWithoutData(),
            mockedDatabase(mockedDaoWithData())
        )

        mockedDataSource.getReviews(0)

        assertEquals(mockedDbReviews.subList(20, 40), mockedDataSource.getReviews(1))
        assertEquals(mockedDbReviews.subList(40, 60), mockedDataSource.getReviews(2))
        assertEquals(listOf<Review>(), mockedDataSource.getReviews(3))
        assertEquals(listOf<Review>(), mockedDataSource.getReviews(10))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun noDataCase() = runTest {
        val mockedDataSource = ReviewsDataSource(
            mockedApiServiceWithoutData(),
            mockedDatabase(mockedDaoWithoutData())
        )

        assertEquals(listOf<Review>(), mockedDataSource.getReviews(0))
        assertEquals(listOf<Review>(), mockedDataSource.getReviews(1))
        assertEquals(listOf<Review>(), mockedDataSource.getReviews(10))
        assertEquals(listOf<Review>(), mockedDataSource.getReviews(100))
    }
}