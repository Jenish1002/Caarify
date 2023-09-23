package com.carserviceapp.carserviceapp.SystemTasks
//package com.carserviceapp.carserviceapp.SYSTEMTASKS
//
//import com.google.firebase.FirebaseException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.PhoneAuthCredential
//import com.google.firebase.auth.PhoneAuthProvider
////class initiateOTPVerification {
////
////}
//
//private lateinit var auth: FirebaseAuth
//private lateinit var verificationId: String
//
//// Function to initiate OTP verification
//private fun initiateOtpVerification(phoneNumber: String) {
//    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            // This callback will be triggered when OTP is automatically detected and verified
//            // You can update UI here
//            updateUIWithVerification(true)
//        }
//
//        override fun onVerificationFailed(e: FirebaseException) {
//            // This callback will be triggered if OTP verification fails
//            // You can update UI here
//            updateUIWithVerification(false)
//        }
//
//        override fun onCodeSent(
//            verificationId: String,
//            token: PhoneAuthProvider.ForceResendingToken
//        ) {
//            // This callback will be triggered when OTP is successfully sent to the user's phone number
//            // You can update UI here
//            this.verificationId = verificationId
//        }
//    }
//
//    // Initiate OTP verification
//    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//        phoneNumber,
//        60, // Timeout duration for OTP verification in seconds
//        java.util.concurrent.TimeUnit.SECONDS,
//        this,
//        callbacks
//    )
//}
//
//// Function to verify OTP
//private fun verifyOtp(otp: String) {
//    val credential = PhoneAuthProvider.getCredential(verificationId, otp)
//    auth.signInWithCredential(credential)
//        .addOnCompleteListener(this) { task ->
//            if (task.isSuccessful) {
//                // OTP verification successful
//                // You can update UI here
//                updateUIWithVerification(true)
//            } else {
//                // OTP verification failed
//                // You can update UI here
//                updateUIWithVerification(false)
//            }
//        }
//}
//
//// Function to update UI based on OTP verification status
//private fun updateUIWithVerification(isVerified: Boolean) {
//    if (isVerified) {
//        // Update UI with green tick or appropriate visual indicator for successful OTP verification
//    } else {
//        // Update UI to indicate that OTP verification failed
//    }
//}
