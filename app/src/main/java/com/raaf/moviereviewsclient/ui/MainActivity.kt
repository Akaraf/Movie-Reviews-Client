package com.raaf.moviereviewsclient.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.raaf.moviereviewsclient.App
import com.raaf.moviereviewsclient.R
import com.raaf.moviereviewsclient.ui.adapters.ReviewsAdapter
import com.raaf.moviereviewsclient.ui.adapters.ReviewsLoaderStateAdapter
import com.raaf.moviereviewsclient.ui.extensions.lazyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}