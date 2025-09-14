package com.travelapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey
    val id: String,
    val name: String,
    val startDate: Date?,
    val endDate: Date?,
    val destination: String?,
    val description: String?,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)
