package com.travelapp.data

import java.util.Date

data class EmailData(
    val id: String,
    val subject: String,
    val sender: String,
    val date: Date,
    val body: String,
    val attachments: List<String> = emptyList(),
    val isTravelRelated: Boolean = false,
    val extractedData: TravelExtractedData? = null
)

data class TravelExtractedData(
    val type: TravelItemType,
    val title: String,
    val description: String?,
    val startDate: Date?,
    val endDate: Date?,
    val location: String?,
    val address: String?,
    val latitude: Double?,
    val longitude: Double?,
    val confirmationNumber: String?,
    val price: String?,
    val airline: String?,
    val hotelName: String?,
    val restaurantName: String?
)
