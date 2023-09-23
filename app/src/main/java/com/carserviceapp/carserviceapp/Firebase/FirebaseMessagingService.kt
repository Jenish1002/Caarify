package com.carserviceapp.carserviceapp.Firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle incoming FCM messages here
        // This method is called when a message is received while the app is in the foreground or background
    }

    override fun onNewToken(token: String) {
        // Handle token refreshes here
        // This method is called when a new token is generated or an existing one is refreshed
    }
}
