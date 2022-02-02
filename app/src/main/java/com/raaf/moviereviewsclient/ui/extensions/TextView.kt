package com.raaf.moviereviewsclient.ui.extensions

import android.view.View.GONE
import android.widget.TextView

fun TextView.setTextOrGone(text: String?) {
    if (!text.isNullOrEmpty()) this.text = text
    else this.visibility = GONE
}

fun TextView.setTextOrGone(prefixText: String, text: String?) {
    if (!text.isNullOrEmpty()) this.text = "$prefixText $text"
    else this.visibility = GONE
}