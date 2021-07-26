package com.kartikcd.mediumx

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val imageView = findViewById(R.id.splashImageView) as ImageView
        val animation = AnimationUtils.loadAnimation(this, R.anim.side_slide)
        imageView.startAnimation(animation)
        val callback = Runnable {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }

        Handler().postDelayed(callback, 1500)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}