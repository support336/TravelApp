package com.travelapp.data.dao

import androidx.room.*
import com.travelapp.data.PointOfInterest
import com.travelapp.data.POICategory
import kotlinx.coroutines.flow.Flow

@Dao
interface PointOfInterestDao {
    @Query("SELECT * FROM points_of_interest ORDER BY name ASC")
    fun getAllPOIs(): Flow<List<PointOfInterest>>

    @Query("SELECT * FROM points_of_interest WHERE id = :id")
    suspend fun getPOIById(id: String): PointOfInterest?

    @Query("SELECT * FROM points_of_interest WHERE category = :category ORDER BY name ASC")
    fun getPOIsByCategory(category: POICategory): Flow<List<PointOfInterest>>

    @Query("SELECT * FROM points_of_interest WHERE name LIKE :query OR description LIKE :query ORDER BY name ASC")
    fun searchPOIs(query: String): Flow<List<PointOfInterest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPOI(poi: PointOfInterest)

    @Update
    suspend fun updatePOI(poi: PointOfInterest)

    @Delete
    suspend fun deletePOI(poi: PointOfInterest)

    @Query("DELETE FROM points_of_interest WHERE id = :id")
    suspend fun deletePOIById(id: String)
}
