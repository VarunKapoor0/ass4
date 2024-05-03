package com.example.assignment4.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.assignment4.R

class SplashScreenActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 2000 // 2 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            // Start MainActivity after delay
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

            // Close SplashScreenActivity to prevent going back to it
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }
}