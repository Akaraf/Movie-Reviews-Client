package com.raaf.moviereviewsclient.ui.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raaf.moviereviewsclient.R
import com.raaf.moviereviewsclient.dataModels.Review

class ReviewsAdapter(private val layoutManager: LinearLayoutManager, private val reviewRV: RecyclerView) : PagingDataAdapter<Review, ReviewsAdapter.ReviewsViewHolder>(
    ReviewDiffItemCallback
) {

//    This variable contains value of the saved position obtained from SavedStateHandle
    private var savedPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsViewHolder =
        ReviewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_review, parent, false))

    override fun onBindViewHolder(holderReviews: ReviewsViewHolder, position: Int) {
        if (position == 0) scrollToSavedPosition()
        val currentReview = getItem(position)
        Glide.with(holderReviews.itemView)
            .load(currentReview?.multimedia?.url ?: "")
            .error(ColorDrawable(Color.GRAY))
            .centerCrop()
            .into(holderReviews.reviewImage)
        if (currentReview != null) {
            holderReviews.reviewName.text = currentReview.filmName
            holderReviews.reviewDescription.text = currentReview.reviewContent
        }
    }

//    Restores scrolling after killing process app
    private fun scrollToSavedPosition() {
        if (savedPosition != null) {
            layoutManager.smoothScrollToPosition(reviewRV, null, savedPosition!!)
            savedPosition = null
        }
    }

    fun setSavedPosition(position: Int) {
        savedPosition = position
    }

    inner class ReviewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val reviewImage: ImageView = itemView.findViewById(R.id.review_i_v)
        val reviewName: TextView = itemView.findViewById(R.id.review_name_t_v)
        val reviewDescription: TextView = itemView.findViewById(R.id.review_description_t_v)
    }
}

object ReviewDiffItemCallback : DiffUtil.ItemCallback<Review>() {

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.multimedia.url == newItem.multimedia.url &&
                oldItem.filmName == newItem.filmName
    }

    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}