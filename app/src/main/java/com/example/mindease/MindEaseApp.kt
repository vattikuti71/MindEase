package com.example.mindease

import android.app.Application
import com.google.firebase.FirebaseApp

class MindEaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}