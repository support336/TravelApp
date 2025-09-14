package com.travelapp.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelapp.data.PointOfInterest
import com.travelapp.data.POICategory
import com.travelapp.data.POIStorage
import com.travelapp.data.repository.TravelRepository
import kotlinx.coroutines.launch
import java.util.*

class MapViewModel : ViewModel() {

    private val repository = TravelRepository()

    private val _pois = MutableLiveData<List<PointOfInterest>>()
    val pois: LiveData<List<PointOfInterest>> = _pois

    init {
        // Don't load POIs until repository is initialized
    }

    fun initializeRepository(context: android.content.Context) {
        repository.initialize(context)
        loadPOIs()
    }

    fun loadPOIs() {
        viewModelScope.launch {
            try {
                // Use shared POI storage
                val poisList = POIStorage.getAllPOIs()
                _pois.value = poisList
                // Debug: Log the number of POIs loaded
                android.util.Log.d("MapViewModel", "Loaded ${poisList.size} POIs")
            } catch (e: Exception) {
                // Handle error
                _pois.value = emptyList()
                android.util.Log.e("MapViewModel", "Error loading POIs", e)
            }
        }
    }

    fun savePOI(
        name: String,
        description: String,
        category: POICategory,
        latitude: Double,
        longitude: Double,
        address: String? = null,
        rating: Float? = null,
        hours: String? = null
    ) {
        viewModelScope.launch {
            try {
                val poi = PointOfInterest(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    description = description,
                    category = category,
                    latitude = latitude,
                    longitude = longitude,
                    address = address,
                    rating = rating,
                    hours = hours,
                    phoneNumber = null,
                    website = null,
                    isOpen = null
                )
                // Save to shared POI storage
                POIStorage.addPOI(poi)
                android.util.Log.d("MapViewModel", "POI saved: $name")
                // Reload POIs to update the UI
                loadPOIs()
            } catch (e: Exception) {
                // Handle error - you can add logging here
                android.util.Log.e("MapViewModel", "Error saving POI", e)
                e.printStackTrace()
            }
        }
    }

    fun deletePOI(poi: PointOfInterest) {
        viewModelScope.launch {
            try {
                POIStorage.removePOI(poi)
                loadPOIs()
            } catch (e: Exception) {
                // Handle error
                android.util.Log.e("MapViewModel", "Error deleting POI", e)
            }
        }
    }
}
