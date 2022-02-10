package com.raaf.moviereviewsclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.raaf.moviereviewsclient.Paging
import com.raaf.moviereviewsclient.data.ReviewsPagingSource
import com.raaf.moviereviewsclient.data.ReviewsPagingSourceProvider
import com.raaf.moviereviewsclient.dataModels.Review
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

class ReviewsViewModel @AssistedInject constructor(
    private var pagingSourceProvider: ReviewsPagingSourceProvider,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val PAGING_STATE = "saved offset"
    }

    private var pagingSource: ReviewsPagingSource? = null
    set(value) {
        field = value
        isNeedToClearReviews = pagingSource?.isNeedToRefillingUIFlow
        isNeedToShowToast = pagingSource?.isNeedToShowToastFlow
        initPagingFlows()
    }
    private var savedPosition: Int? = null
    private val pagerConfig = PagingConfig(
        pageSize = Paging.PAGE_SIZE,
        initialLoadSize = Paging.INITIAL_LOAD_SIZE,
        enablePlaceholders = true)

    var isNeedToClearReviews: MutableSharedFlow<Boolean>? = null
    var isNeedToShowToast: MutableSharedFlow<Boolean>? = null

    init {
        initPagingSource()
        useSavedOffset()
    }

    var reviews: Flow<PagingData<Review>>? = null

    fun setNewPagingSource() {
        initPagingSource()
    }

    private fun initPagingFlows() {
        reviews = Pager(
            pagerConfig
        ) {
            pagingSource!!
        }.flow.cachedIn(viewModelScope)
    }

    private fun initPagingSource() {
        pagingSource = pagingSourceProvider.providePagingSource()
    }

    private fun useSavedOffset() {
        val offset = savedStateHandle.get<Int?>(PAGING_STATE)
        pagingSource?.savedPage = offset?.div(Paging.PAGE_SIZE)
        savedPosition = offset?.rem(Paging.PAGE_SIZE)
    }

    fun getSavedPosition() : Int? {
        val result = savedPosition
        savedPosition = null
        return result
    }

    fun saveOffset(currentPosition: Int?) {
        savedStateHandle.set(PAGING_STATE, currentPosition)
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle) : ReviewsViewModel
    }
}