package com.travelapp

import android.app.Application
import com.travelapp.data.repository.TravelRepository

class TravelApplication : Application() {
    
    val travelRepository = TravelRepository()
    
    override fun onCreate() {
        super.onCreate()
        travelRepository.initialize(this)
    }
}
