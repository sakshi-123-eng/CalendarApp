package com.example.calendarapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CalenderApplication : Application() {

    override fun onCreate() {
        super.onCreate()

    }
}