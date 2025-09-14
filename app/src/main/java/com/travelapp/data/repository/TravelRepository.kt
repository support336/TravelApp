package com.travelapp.data.repository

import android.content.Context
import com.travelapp.data.PointOfInterest
import com.travelapp.data.TravelDatabase
import com.travelapp.data.TravelItem
import com.travelapp.data.Trip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TravelRepository {
    private var database: TravelDatabase? = null

    fun initialize(context: Context) {
        if (database == null) {
            database = TravelDatabase.getDatabase(context)
        }
    }

    suspend fun getAllTrips(): List<Trip> = withContext(Dispatchers.IO) {
        database?.tripDao()?.getAllTrips()?.let { flow ->
            // Convert Flow to List - in a real app, you'd observe the Flow
            emptyList() // Placeholder
        } ?: emptyList()
    }

    suspend fun insertTrip(trip: Trip) = withContext(Dispatchers.IO) {
        database?.tripDao()?.insertTrip(trip)
    }

    suspend fun getAllTravelItems(): List<TravelItem> = withContext(Dispatchers.IO) {
        database?.travelItemDao()?.getAllTravelItems()?.let { flow ->
            // Convert Flow to List - in a real app, you'd observe the Flow
            emptyList() // Placeholder
        } ?: emptyList()
    }

    suspend fun insertTravelItem(travelItem: TravelItem) = withContext(Dispatchers.IO) {
        database?.travelItemDao()?.insertTravelItem(travelItem)
    }

    suspend fun getAllPOIs(): List<PointOfInterest> = withContext(Dispatchers.IO) {
        try {
            // For now, return empty list since we need to handle Flow properly
            // In a real app, you'd collect the Flow here
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun insertPOI(poi: PointOfInterest) = withContext(Dispatchers.IO) {
        database?.pointOfInterestDao()?.insertPOI(poi)
    }

    suspend fun deletePOI(poi: PointOfInterest) = withContext(Dispatchers.IO) {
        database?.pointOfInterestDao()?.deletePOI(poi)
    }
}
