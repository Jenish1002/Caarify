package com.carserviceapp.carserviceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import com.carserviceapp.carserviceapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)
        mAuth = FirebaseAuth.getInstance()

        // Set cross-fade transition for the whole Activity
        val crossFadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        crossFadeAnimation.duration = 2000 // 2 seconds duration
        findViewById<View>(android.R.id.content).startAnimation(crossFadeAnimation)

        mainBinding.btngetstarted.setOnClickListener {
            val intent = Intent(this@MainActivity, SignUp::class.java)
            startActivity(intent)
        }
        mainBinding.btnsignin.setOnClickListener {
            val intent = Intent(this@MainActivity, SignIn::class.java)
            startActivity(intent)
        }
        // keep user signIn
        if (mAuth.currentUser != null && mAuth.currentUser!!.isEmailVerified) {
            //val user = mAuth.currentUser
            val intent = Intent(this@MainActivity, Dashboard::class.java)
            startActivity(intent)
            finish()
        } else {
            // DO NOTHING
            //Toast.makeText(this, "Please verify your email.", Toast.LENGTH_SHORT).show()
        }
    }
}