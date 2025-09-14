package com.travelapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "travel_items")
data class TravelItem(
    @PrimaryKey
    val id: String,
    val tripId: String,
    val title: String,
    val description: String?,
    val type: TravelItemType,
    val startDate: Date?,
    val endDate: Date?,
    val location: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val confirmationNumber: String?,
    val source: String, // "calendar", "gmail", "manual"
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class TravelItemType {
    FLIGHT,
    HOTEL,
    RESTAURANT,
    ACTIVITY,
    TRANSPORTATION,
    OTHER
}
