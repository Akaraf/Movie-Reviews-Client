package com.raaf.moviereviewsclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.raaf.moviereviewsclient.Paging
import com.raaf.moviereviewsclient.data.ReviewsPagingSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val PAGING_STATE = "saved offset"

class ReviewsViewModel @AssistedInject constructor(
    private val pagingSource: ReviewsPagingSource,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var savedPosition: Int? = null

    init {
        useSavedOffset()
    }

    val reviews = Pager(
        PagingConfig(pageSize = Paging.PAGE_SIZE,
        initialLoadSize = Paging.INITIAL_LOAD_SIZE,
        enablePlaceholders = true)
    ) {
        pagingSource
    }.flow.cachedIn(viewModelScope)

    private fun useSavedOffset() {
        val offset = savedStateHandle.get<Int?>(PAGING_STATE)
        pagingSource.savedPage = offset?.div(Paging.PAGE_SIZE)
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