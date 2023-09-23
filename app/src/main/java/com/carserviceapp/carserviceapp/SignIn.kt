package com.carserviceapp.carserviceapp

//noinspection SuspiciousImport
import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.carserviceapp.carserviceapp.Utils.InternetConnectivityUtil
import com.carserviceapp.carserviceapp.Utils.ProgressBarUtil
import com.carserviceapp.carserviceapp.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignIn : AppCompatActivity() {

    private lateinit var signinBinding: ActivitySigninBinding
   // private lateinit var mAuth: FirebaseAuth
    //var v: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signinBinding = ActivitySigninBinding.inflate(layoutInflater)
        val view = signinBinding.root
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

        // on click events
        signinBinding.btnlogin.setOnClickListener {
            SignInwithEmailPassword()
        }

        // forgot password
        signinBinding.tvforgot.setOnClickListener {
            val intent = Intent(this@SignIn, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    private fun SignInwithEmailPassword() {

        val email = signinBinding.etloginemail.text.toString().trim()
        val password = signinBinding.etloginpass.text.toString()

        // Check that fields are not empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty.", Toast.LENGTH_LONG).show()
            return
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show()
            return
        }

        // SHOW PROGRESS BAR
        ProgressBarUtil.showProgressDialog(this)
        // Validate password length
//        if (password.length < 6) {
//            Toast.makeText(this, "Password too small.", Toast.LENGTH_LONG).show()
//            return
//        }

        // Create a Firebase Auth instance and sign in with the email and password
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                     val user = mAuth.currentUser
                    // check if user mail is verified
                    if (user?.isEmailVerified == true) {
                        val intent = Intent(this@SignIn, Dashboard::class.java)
                        Toast.makeText(this, "Logged In", Toast.LENGTH_LONG).show()
                        startActivity(intent)
                        ProgressBarUtil.hideProgressDialog()
                        finish()
                    } else {
                        //HIDE PROGRESS BAR
                        ProgressBarUtil.hideProgressDialog()
                        Toast.makeText(this, "Please verify your email address", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    //HIDE PROGRESS BAR
                    ProgressBarUtil.hideProgressDialog()
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun animateObjects() {

        //animation code
        signinBinding.etloginemail.animate().translationY(130F).alpha(1F).setDuration(800)
            .setStartDelay(400).start()
        signinBinding.etloginpass.animate().translationY(130F).alpha(1F).setDuration(800)
            .setStartDelay(400).start()
        //signinBinding.btnlogin.animate().translationY(170F).alpha(1F).setDuration(800).setStartDelay(400).start()
        signinBinding.tvforgot.animate().translationY(140F).alpha(1F).setDuration(800)
            .setStartDelay(400).start()
        signinBinding.tvsignin.animate().translationY(80F).alpha(1F).setDuration(800)
            .setStartDelay(400).start()

        signinBinding.btngoogle.animate().translationY(-50f).alpha(1F).setDuration(900)
            .setStartDelay(350).start()
        signinBinding.btnface.animate().translationY(-50f).alpha(1F).setDuration(900)
            .setStartDelay(350).start()
        signinBinding.btntwitter.animate().translationY(-50f).alpha(1F).setDuration(900)
            .setStartDelay(350).start()

        signinBinding.v1.animate().translationY(150F).alpha(1F).setDuration(800).setStartDelay(300)
            .start()
        signinBinding.v2.animate().translationY(150F).alpha(1F).setDuration(800).setStartDelay(300)
            .start()
        signinBinding.tvor.animate().translationY(150F).alpha(1F).setDuration(800)
            .setStartDelay(300).start()
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}