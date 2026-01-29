package com.trebedit.webcraft

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.google.firebase.FirebaseApp

class WebCraftApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus ->
            // Initialization complete
        }
    }
}
