package com.travelapp.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.travelapp.data.converters.DateConverters
import com.travelapp.data.dao.PointOfInterestDao
import com.travelapp.data.dao.TravelItemDao
import com.travelapp.data.dao.TripDao

@Database(
    entities = [Trip::class, TravelItem::class, PointOfInterest::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun travelItemDao(): TravelItemDao
    abstract fun pointOfInterestDao(): PointOfInterestDao

    companion object {
        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "travel_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
