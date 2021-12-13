package com.raaf.moviereviewsclient.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.lifecycle.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raaf.moviereviewsclient.App
import com.raaf.moviereviewsclient.R
import com.raaf.moviereviewsclient.ui.adapters.ReviewsAdapter
import com.raaf.moviereviewsclient.ui.adapters.ReviewsLoaderStateAdapter
import com.raaf.moviereviewsclient.ui.extensions.lazyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var startProgress: ProgressBar
    private lateinit var reviewsRV: RecyclerView
    val mainVM: MainActivityViewModel by lazyViewModel {
        App.reviewsComponent.mainActivityViewModel().create(it)
    }
    private val layoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }
    private val reviewsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ReviewsAdapter(layoutManager, reviewsRV)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startProgress = findViewById(R.id.start_progress_bar)
        reviewsRV = findViewById(R.id.reviews_recycler_view)
        reviewsRV.layoutManager = layoutManager

//        Processing states of data in recycler
//        We can wrap recycler in SwipeRefreshLayout instead of using loader state adapters
        reviewsRV.adapter = reviewsAdapter.withLoadStateHeaderAndFooter(
            ReviewsLoaderStateAdapter { reviewsAdapter.retry() },
            ReviewsLoaderStateAdapter { reviewsAdapter.retry() }
        )

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainVM.reviews.collectLatest(reviewsAdapter::submitData)
            }
        }

        reviewsAdapter.addLoadStateListener {
            if (it.refresh == LoadState.Loading) {
                reviewsRV.visibility = GONE
                startProgress.visibility = VISIBLE
            } else {
                reviewsRV.visibility = VISIBLE
                startProgress.visibility = GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSavedPositionToAdapter(mainVM.getSavedPosition())
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
        val position = layoutManager.findLastVisibleItemPosition()
        if (position > 0) mainVM.saveOffset(position)
    }
}