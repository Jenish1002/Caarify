@file:Suppress("DEPRECATION")

package com.carserviceapp.carserviceapp

//noinspection SuspiciousImport
import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.carserviceapp.carserviceapp.Utils.InternetConnectivityUtil
import com.carserviceapp.carserviceapp.Utils.ProgressBarUtil
import com.carserviceapp.carserviceapp.databinding.ActivityEditprofileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class EditProfile : AppCompatActivity() {

    private lateinit var editprofileBinding: ActivityEditprofileBinding
    val user = FirebaseAuth.getInstance().currentUser
    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("Customer").child(user!!.uid)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editprofileBinding = ActivityEditprofileBinding.inflate(layoutInflater)
        setContentView(editprofileBinding.root)


        // CHECK INTERNET
        val parentLayout = findViewById<View>(R.id.content)
        if (!InternetConnectivityUtil.isInternetAvailable(this)
        ) {
            if (parentLayout != null) {
                InternetConnectivityUtil.showInternetSnackbar(parentLayout, this)
            }
        }

        //Show data
        showUserData()

        editprofileBinding.btnSave.setOnClickListener {
            performDataUpdate()
        }
        editprofileBinding.btnCancel.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun showUserData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val name = dataSnapshot.child("name").value.toString()
                    val address = dataSnapshot.child("address").value.toString()
                    val phone = dataSnapshot.child("mobile").value.toString()
                    val license = dataSnapshot.child("license").value.toString()

                    editprofileBinding.etUserName.setText(name)
                    Log.e("etusername", "it is displaying")
                    editprofileBinding.etAddress.setText(address)
                    editprofileBinding.etPhone.setText(phone)
                    editprofileBinding.etLicense.setText(license)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                //Log.e("etusername", "it is not displaying" + databaseError)
            }
        })
    }

    private fun performDataUpdate() {

        val name = editprofileBinding.etUserName.text.toString()
        val address = editprofileBinding.etAddress.text.toString()
        val phone = editprofileBinding.etPhone.text.toString()
        val license = editprofileBinding.etLicense.text.toString().toUpperCase(Locale.ROOT)

        if (name.isEmpty() || address.isEmpty() || phone.isEmpty() || license.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty.", Toast.LENGTH_LONG).show()
            return
        }
        if (phone.length != 10) {
            Toast.makeText(this, "Phone length must be 10 digits", Toast.LENGTH_LONG).show()
            return
        }
        if (!isValidLicenseNumber(license)) {
            Toast.makeText(this, "Enter valid license number", Toast.LENGTH_LONG).show()
            return
        }
        if (!isValidPhoneNumber(phone)) {
            Toast.makeText(this, "Enter valid phone number", Toast.LENGTH_LONG).show()
            return
        }
        if (!nameIsValid(name)) {
            Toast.makeText(this, "Name must not have special symbols", Toast.LENGTH_LONG).show()
            return
        }

        val customerData = HashMap<String, Any>()
        customerData["name"] = name
        customerData["address"] = address
        customerData["mobile"] = phone
        customerData["license"] = license

        databaseReference.updateChildren(customerData).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                //PROGRESS BAR
                ProgressBarUtil.showProgressDialog(this)

                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
                super.onBackPressed()

            } else {
                Toast.makeText(this, "Could not Update", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidLicenseNumber(license: String): Boolean {
            val licensePattern = "^[A-Z]{1,2}\\d{13}[A-Z]{0,2}$"
            return license.matches(licensePattern.toRegex())
    }





    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        //val phonePattern = "^[+]?[0-9]{10,13}$"
        //val phonePattern = "^(?!0{10,13}$)[+]?[0-9]{10,13}$"
        val phonePattern =
            "^(?!([0-9])\\1{9,12}$)[+]?[0-9]{10,13}$" // does not allow repetition of any number for 10 times
        return phoneNumber.matches(phonePattern.toRegex())
    }

    private fun nameIsValid(name: String): Boolean {
        val regexPattern = "^[a-zA-Z]+$".toRegex()
        return name.matches(regexPattern)
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


