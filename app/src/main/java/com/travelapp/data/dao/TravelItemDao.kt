package com.travelapp.data.dao

import androidx.room.*
import com.travelapp.data.TravelItem
import com.travelapp.data.TravelItemType
import kotlinx.coroutines.flow.Flow

@Dao
interface TravelItemDao {
    @Query("SELECT * FROM travel_items ORDER BY startDate ASC")
    fun getAllTravelItems(): Flow<List<TravelItem>>

    @Query("SELECT * FROM travel_items WHERE tripId = :tripId ORDER BY startDate ASC")
    fun getTravelItemsByTrip(tripId: String): Flow<List<TravelItem>>

    @Query("SELECT * FROM travel_items WHERE id = :id")
    suspend fun getTravelItemById(id: String): TravelItem?

    @Query("SELECT * FROM travel_items WHERE type = :type ORDER BY startDate ASC")
    fun getTravelItemsByType(type: TravelItemType): Flow<List<TravelItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTravelItem(travelItem: TravelItem)

    @Update
    suspend fun updateTravelItem(travelItem: TravelItem)

    @Delete
    suspend fun deleteTravelItem(travelItem: TravelItem)

    @Query("DELETE FROM travel_items WHERE id = :id")
    suspend fun deleteTravelItemById(id: String)

    @Query("DELETE FROM travel_items WHERE tripId = :tripId")
    suspend fun deleteTravelItemsByTrip(tripId: String)
}
