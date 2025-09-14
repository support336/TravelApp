package com.travelapp.ui.points

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelapp.data.PointOfInterest
import com.travelapp.data.POIStorage
import com.travelapp.data.repository.TravelRepository
import kotlinx.coroutines.launch

class PointsViewModel : ViewModel() {

    private val repository = TravelRepository()

    private val _pois = MutableLiveData<List<PointOfInterest>>()
    val pois: LiveData<List<PointOfInterest>> = _pois

    init {
        loadPOIs()
    }

    fun loadPOIs() {
        viewModelScope.launch {
            try {
                val poisList = POIStorage.getAllPOIs()
                _pois.value = poisList
            } catch (e: Exception) {
                // Handle error
                _pois.value = emptyList()
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
            }
        }
    }
}
