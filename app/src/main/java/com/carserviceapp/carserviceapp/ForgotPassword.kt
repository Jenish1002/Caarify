package com.carserviceapp.carserviceapp

import android.R
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.carserviceapp.carserviceapp.Utils.InternetConnectivityUtil
import com.carserviceapp.carserviceapp.databinding.ActivityForgotpasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var activityForgotpasswordBinding: ActivityForgotpasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgotpasswordBinding = ActivityForgotpasswordBinding.inflate(layoutInflater)
        setContentView(activityForgotpasswordBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        activityForgotpasswordBinding.btnSendResetLink.setOnClickListener {
            val email = activityForgotpasswordBinding.etForgotEmail.text.toString().trim()

            if (email.isEmpty()){
                Toast.makeText(this, "Field must not be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else{
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {task->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Reset Password mail sent", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "No such user exists", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }

        // CHECK INTERNET
        val parentLayout = findViewById<View>(R.id.content)
        if (!InternetConnectivityUtil.isInternetAvailable(this)
        ) {
            if (parentLayout != null) {
                InternetConnectivityUtil.showInternetSnackbar(parentLayout, this)
            }
        }
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

