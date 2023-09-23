//
@file:Suppress("DEPRECATION")

package com.carserviceapp.carserviceapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import com.carserviceapp.carserviceapp.databinding.ActivitySplashscreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var splashscreenBinding: ActivitySplashscreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashscreenBinding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(splashscreenBinding.root)

        // Car animation
        val imageView = findViewById<ImageView>(R.id.iv_Car)
        val fadeAnimation = AlphaAnimation(0.3f, 1.9f)
        fadeAnimation.duration = 2000 // 2 seconds duration
        imageView.startAnimation(fadeAnimation)

        // Navigate to the main activity after 2 seconds
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 2 seconds delay
    }
}


