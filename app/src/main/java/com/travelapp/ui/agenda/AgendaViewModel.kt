package com.travelapp.ui.agenda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travelapp.data.Trip
import com.travelapp.data.TravelItem
import com.travelapp.data.repository.TravelRepository
import kotlinx.coroutines.launch

class AgendaViewModel : ViewModel() {

    private val repository = TravelRepository()

    private val _isSignedIn = MutableLiveData<Boolean>()
    val isSignedIn: LiveData<Boolean> = _isSignedIn

    private val _isSyncing = MutableLiveData<Boolean>()
    val isSyncing: LiveData<Boolean> = _isSyncing

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips

    init {
        checkSignInStatus()
        loadTrips()
    }

    private fun checkSignInStatus() {
        // TODO: Check Google sign-in status
        _isSignedIn.value = false
    }

    fun signInWithGoogle() {
        // TODO: Implement Google sign-in
        _isSignedIn.value = true
        syncTravelData()
    }

    private fun syncTravelData() {
        viewModelScope.launch {
            _isSyncing.value = true
            try {
                // TODO: Sync with Google Calendar and Gmail
                loadTrips()
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isSyncing.value = false
            }
        }
    }

    private fun loadTrips() {
        viewModelScope.launch {
            try {
                val tripsList = repository.getAllTrips()
                _trips.value = tripsList
            } catch (e: Exception) {
                // Handle error
                _trips.value = emptyList()
            }
        }
    }

    fun addTravelItem(tripId: String, travelItem: TravelItem) {
        viewModelScope.launch {
            try {
                repository.insertTravelItem(travelItem)
                loadTrips()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
