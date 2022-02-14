package com.raaf.moviereviewsclient.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raaf.moviereviewsclient.App
import com.raaf.moviereviewsclient.R
import com.raaf.moviereviewsclient.ui.adapters.ReviewsAdapter
import com.raaf.moviereviewsclient.ui.adapters.ReviewsLoaderStateAdapter
import com.raaf.moviereviewsclient.ui.extensions.lazyViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReviewsFragment : Fragment() {

    private lateinit var loadStateIncludeLayout : FrameLayout
    private lateinit var startProgressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var retryButton: Button
    private lateinit var reviewsRV: RecyclerView

    val reviewsVM: ReviewsViewModel by lazyViewModel {
        App.reviewsComponent.reviewsViewModel().create(it)
    }
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var reviewsAdapter: ReviewsAdapter

    private var isNeedToShowToastJob: Job? = null
    private var isNeedToCleanReviewsJob: Job? = null
    private var reviewsJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reviews, container, false)
        loadStateIncludeLayout = view.findViewById(R.id.reviews_load_state_include_layout)
        startProgressBar = loadStateIncludeLayout.findViewById(R.id.reviews_load_progress_bar)
        errorLayout = loadStateIncludeLayout.findViewById(R.id.error_layout)
        retryButton = loadStateIncludeLayout.findViewById(R.id.retry_button)
        reviewsRV = view.findViewById(R.id.reviews_recycler_view)
        layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        reviewsRV.layoutManager = layoutManager
        reviewsAdapter = ReviewsAdapter(layoutManager, reviewsRV)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setJobsFromPagingSource()
//        Processing states of data in recycler
//        We can wrap recycler in SwipeRefreshLayout instead of using loader state adapters
        reviewsRV.adapter = reviewsAdapter.withLoadStateHeaderAndFooter(
            ReviewsLoaderStateAdapter { reviewsAdapter.retry() },
            ReviewsLoaderStateAdapter { reviewsAdapter.retry() }
        )

        reviewsAdapter.addLoadStateListener {
            if (it.refresh == LoadState.Loading && reviewsAdapter.itemCount == 0) {
                reviewsRV.visibility = GONE
                errorLayout.visibility = GONE
                startProgressBar.visibility = VISIBLE
            }
            if (it.refresh != LoadState.Loading && reviewsAdapter.itemCount == 0) {
                reviewsRV.visibility = GONE
                startProgressBar.visibility = GONE
                errorLayout.visibility = VISIBLE
            }
            if (reviewsAdapter.itemCount != 0) {
                reviewsRV.visibility = VISIBLE
                loadStateIncludeLayout.visibility = GONE
            }
        }

        retryButton.setOnClickListener {
            reviewsAdapter.retry()
        }

        setSavedPositionToAdapter(reviewsVM.getSavedPosition())
    }

    private fun setNewPagingSource() {
        setJobsFromPagingSource()
        reviewsVM.setNewPagingSource()
    }

    private fun setJobsFromPagingSource() {
        isNeedToShowToastJob?.cancel()
        isNeedToShowToastJob = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                reviewsVM.isNeedToShowToast?.onEach {
                    if (it) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.no_internet_connection),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }?.collect()
            }
        }
        isNeedToCleanReviewsJob?.cancel()
        isNeedToCleanReviewsJob = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                reviewsVM.isNeedToClearReviews?.onEach {
                    if (it) {
                        reviewsAdapter.submitData(lifecycle, PagingData.empty())
                        reviewsRV.adapter?.notifyDataSetChanged()
                        setNewPagingSource()
                    }
                }?.collect()
            }
        }
        reviewsJob?.cancel()
        reviewsJob = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                reviewsVM.reviews?.collectLatest(reviewsAdapter::submitData)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        savingPagingState()
    }

    private fun setSavedPositionToAdapter(position: Int?) {
        if (position != null) reviewsAdapter.setSavedPosition(position)
    }

    //    Passing value to be saved in SavedStateHandle
    private fun savingPagingState() {
        val position = layoutManager.findLastVisibleItemPosition() ?: 0
        if (position > 0) reviewsVM.saveOffset(position)
    }
}