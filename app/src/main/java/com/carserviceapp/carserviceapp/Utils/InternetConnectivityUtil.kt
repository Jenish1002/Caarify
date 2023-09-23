package com.carserviceapp.carserviceapp.Utils

import android.R
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import com.google.android.material.snackbar.Snackbar

object InternetConnectivityUtil {

    // INTERNET AVAILABILITY
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        } else {
            @Suppress("DEPRECATION")
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo?.isConnected ?: false
        }
    }

    // SNACKBAR TO CHECK INTERNET CONNECTIVITY
    fun showInternetSnackbar(view: View,context: Context) {
        val snackbar = Snackbar.make(
            view,
            "Please check your internet connectivity.",
            Snackbar.LENGTH_INDEFINITE
        )

        snackbar.setAction("Retry") {
            if (isInternetAvailable(context)) {
                snackbar.dismiss()
            } else {
                showInternetSnackbar(view,context)
            }
        }
        snackbar.show()
    }
}
