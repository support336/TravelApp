package com.travelapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "points_of_interest")
data class PointOfInterest(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val category: POICategory,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val rating: Float?,
    val hours: String?,
    val phoneNumber: String?,
    val website: String?,
    val isOpen: Boolean?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class POICategory {
    RESTAURANT,
    ATTRACTION,
    HOTEL,
    SHOPPING,
    TRANSPORTATION,
    ENTERTAINMENT,
    NATURE,
    CULTURE,
    OTHER
}
