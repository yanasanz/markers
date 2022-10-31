package com.example.maptest.application

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MarkersApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("MAPS_API_KEY")
    }
}