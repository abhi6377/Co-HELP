package com.app_dev.co_help

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class SplashActivity : AppCompatActivity() {
    private val SPLASH_SCREEN_TIMEOUT: Long = 2000

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        window.navigationBarColor = ContextCompat.getColor(this, R.color.splash)

        Handler().postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_TIMEOUT)
    }
}