package com.raaf.moviereviewsclient.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.raaf.moviereviewsclient.dataModels.Review
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DetailReviewViewModel @AssistedInject constructor(
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var review: Review? = null

    fun getReview() : Review? = review

    fun setReviewFromBundle(bundleReview: Review?) {
        if (bundleReview != null) review = bundleReview
    }

    @AssistedFactory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle) : DetailReviewViewModel
    }
}