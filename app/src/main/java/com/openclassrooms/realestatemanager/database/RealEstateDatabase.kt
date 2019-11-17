package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.models.Estate

@Database(entities = [Estate::class], version = 1, exportSchema = false)
abstract class RealEstateDatabase : RoomDatabase() {

    abstract fun estateDao(): EstateDao

    companion object {
        private var INSTANCE: RealEstateDatabase? = null

        fun getInstance(context: Context): RealEstateDatabase {
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        RealEstateDatabase::class.java,"realEstateManager.db").build()
                }
            }
            return INSTANCE as RealEstateDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}