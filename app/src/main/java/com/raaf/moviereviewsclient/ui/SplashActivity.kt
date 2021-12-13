package com.raaf.moviereviewsclient.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.raaf.moviereviewsclient.R

/* This activity is intended only for display splash screen and should not contain other data or functionality.
   Splash screen can be implemented without SplashActivity - just use setTheme(R.style.Theme_MovieReviewsClient)
   in MainActivity before super.onCreate(...). */

class SplashActivity : AppCompatActivity() {

    private var isRecreated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MovieReviewsClient)
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {

    }
}