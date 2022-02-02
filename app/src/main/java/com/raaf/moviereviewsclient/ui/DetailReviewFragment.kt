package com.raaf.moviereviewsclient.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.imageview.ShapeableImageView
import com.raaf.moviereviewsclient.App
import com.raaf.moviereviewsclient.R
import com.raaf.moviereviewsclient.Review.PARCELABLE_REVIEW
import com.raaf.moviereviewsclient.dataModels.Review
import com.raaf.moviereviewsclient.ui.extensions.lazyViewModel
import com.raaf.moviereviewsclient.ui.extensions.setTextOrGone

class DetailReviewFragment : Fragment() {

    val detailReviewVM: DetailReviewViewModel by lazyViewModel {
        App.reviewsComponent.detailReviewViewModel().create(it)
    }

    lateinit var image: ShapeableImageView

    lateinit var filmNameTV: TextView
    lateinit var filmOpeningDateTV: TextView
    lateinit var ratingMpaaTV: TextView

    lateinit var filmLayout: LinearLayout
    lateinit var filmDivider: View

    lateinit var reviewHeadlineTV: TextView
    lateinit var authorNameTV: TextView
    lateinit var reviewPublicationDate: TextView
    lateinit var reviewSummaryShortTV: TextView

    lateinit var openInNYT: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailReviewVM.setReviewFromBundle(
            arguments?.getParcelable<Review?>(PARCELABLE_REVIEW))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_detail_review, container, false)
        image = view.findViewById(R.id.film_image)
        filmNameTV = view.findViewById(R.id.film_name_t_v)
        filmOpeningDateTV = view.findViewById(R.id.film_opening_date_t_v)
        ratingMpaaTV = view.findViewById(R.id.rating_mpaa_t_v)
        filmLayout = view.findViewById(R.id.detail_film_layout)
        filmDivider = view.findViewById(R.id.detail_film_divider)
        reviewHeadlineTV = view.findViewById(R.id.review_headline_t_v)
        authorNameTV = view.findViewById(R.id.name_author_t_v)
        reviewPublicationDate = view.findViewById(R.id.review_publication_date_t_v)
        reviewSummaryShortTV = view.findViewById(R.id.review_summary_short)
        openInNYT = view.findViewById(R.id.open_nyt_fab)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailReviewVM.getReview()?.let { fillFilmUI(it) }
        detailReviewVM.getReview()?.let { fillReviewUI(it) }
    }


    private fun fillFilmUI(review: Review) {
        Glide.with(this.requireContext())
            .load(review.multimedia.url)
            .error(ColorDrawable(Color.GRAY))
            .into(image)
        filmNameTV.setTextOrGone(review.filmName)
        filmOpeningDateTV.setTextOrGone(review.movieOpeningDate)
        ratingMpaaTV.setTextOrGone(getString(R.string.rating), review.mpaaRating)
        filmLayoutVisibility(review)
    }

    private fun fillReviewUI(review: Review) {
        reviewHeadlineTV.setTextOrGone(review.headline)
        authorNameTV.setTextOrGone(getString(R.string.by), review.authorName)
        reviewPublicationDate.setTextOrGone(review.reviewLastUpdateDate)
        reviewSummaryShortTV.setTextOrGone(review.reviewContent)
        openInNYT.setOnClickListener {
            val address = Uri.parse(review.link.reviewUrl)
            val openIntent = Intent(Intent.ACTION_VIEW, address)
            if (openIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(openIntent)
            }
        }
    }

    private fun filmLayoutVisibility(review: Review) {
        if (review.filmName.isEmpty() && review.mpaaRating.isNullOrBlank()
            && review.movieOpeningDate.isNullOrEmpty()) {
            filmLayout.visibility = GONE
            filmDivider.visibility = GONE
        }
    }
}