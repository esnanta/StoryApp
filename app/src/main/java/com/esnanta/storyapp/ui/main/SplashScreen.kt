package com.esnanta.storyapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.esnanta.storyapp.R


class SplashScreen : AppCompatActivity() {

    private val duration = 4000 // Splash screen duration in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextPage()
        }, duration.toLong())
    }

    private fun navigateToNextPage() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}