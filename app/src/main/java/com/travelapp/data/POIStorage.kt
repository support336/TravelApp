package com.travelapp.data

object POIStorage {
    private val savedPOIs = mutableListOf<PointOfInterest>()
    
    fun addPOI(poi: PointOfInterest) {
        savedPOIs.add(poi)
    }
    
    fun removePOI(poi: PointOfInterest) {
        savedPOIs.remove(poi)
    }
    
    fun getAllPOIs(): List<PointOfInterest> {
        return savedPOIs.toList()
    }
    
    fun clearPOIs() {
        savedPOIs.clear()
    }
}
