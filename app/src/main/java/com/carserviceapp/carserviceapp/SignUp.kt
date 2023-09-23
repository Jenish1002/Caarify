package com.carserviceapp.carserviceapp

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.carserviceapp.carserviceapp.Models.CustomerData
import com.carserviceapp.carserviceapp.Utils.InternetConnectivityUtil
import com.carserviceapp.carserviceapp.Utils.ProgressBarUtil
import com.carserviceapp.carserviceapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    private lateinit var signupBinding: ActivitySignupBinding
    val database = FirebaseDatabase.getInstance()
    val customersRef = database.getReference("Customer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        // CHECK INTERNET
        val parentLayout = findViewById<View>(R.id.content)
        if (!InternetConnectivityUtil.isInternetAvailable(this)
        ) {
            if (parentLayout != null) {
                InternetConnectivityUtil.showInternetSnackbar(parentLayout, this)
            }
        }

        // Animations
        animateObjects()

        // SignUp on confirm
        signupBinding.btnConfirm.setOnClickListener {
            EmailandPassword()
        }

    }

    private fun animateObjects() {
        // animation code
        signupBinding.btngoogle.animate().translationY(-50f).alpha(1F).setDuration(900)
            .setStartDelay(350).start()
        signupBinding.btnface.animate().translationY(-50f).alpha(1F).setDuration(900)
            .setStartDelay(350).start()
        signupBinding.btntwitter.animate().translationY(-50f).alpha(1F).setDuration(900)
            .setStartDelay(350).start()
        signupBinding.etemail.animate().translationY(170F).alpha(1F).setDuration(800)
            .setStartDelay(300).start()
        signupBinding.etpass.animate().translationY(170F).alpha(1F).setDuration(800)
            .setStartDelay(350).start()
        signupBinding.etcpass.animate().translationY(170F).alpha(1F).setDuration(800)
            .setStartDelay(400).start()
        signupBinding.tvSignUp.animate().translationY(80F).alpha(1F).setDuration(800)
            .setStartDelay(400).start()

        signupBinding.v1.animate().translationY(150F).alpha(1F).setDuration(800).setStartDelay(300)
            .start()
        signupBinding.v2.animate().translationY(150F).alpha(1F).setDuration(800).setStartDelay(300)
            .start()
        signupBinding.tvOr.animate().translationY(150F).alpha(1F).setDuration(800)
            .setStartDelay(300).start()

    }

    private fun EmailandPassword() {
        val email = signupBinding.etemail.text.toString().trim()
        val password = signupBinding.etpass.text.toString()
        val confirmPassword = signupBinding.etcpass.text.toString()
//        val Cust_Adderess = "null"
//        val Cust_Email = "null"
//        val Cust_License = "null"
//        val Cust_Mobile = "null"
//        val Cust_Name = "null"

        // Check that fields are not empty
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty.", Toast.LENGTH_LONG).show()
            return
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show()
            return
        }

        // Validate password length
        if (password.length < 6) {
            Toast.makeText(this, "Password too small.", Toast.LENGTH_LONG).show()
            return
        }

        // Check that password and confirm password match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show()
            return
        }

        // PROGRESS BAR
        ProgressBarUtil.showProgressDialog(this)

        // Create a Firebase Auth instance and use it to create a new user with the email and password
        val mAuth = FirebaseAuth.getInstance()
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    user?.sendEmailVerification()?.addOnSuccessListener {
                        Toast.makeText(
                            this,
                            "Verification email sent to ${user.email}. Please verify your email",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(this@SignUp, SignIn::class.java)
                        startActivity(intent)
                        finish()

                        // HIDE PROGRESS BAR
                        ProgressBarUtil.hideProgressDialog()

                        // Add user to Realtime Database
                        val customerData = CustomerData(
                            address = "",
                            email = email,
                            license = "",
                            mobile = "",
                            name = ""
                        )

                        customersRef.child(user.uid).setValue(customerData)
                        val user = mAuth.currentUser
                        if (user?.isEmailVerified == true) {
                            database.reference.child("Customer").child(user.uid)
                                .setValue(customerData).addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Customer created",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        this,
                                        "unable to create customer",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                        else{
                            Toast.makeText(this, "please verify email.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }.addOnFailureListener { e ->
                //PROGRESS BAR
                ProgressBarUtil.hideProgressDialog()
                Toast.makeText(
                    this,
                    " ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}



