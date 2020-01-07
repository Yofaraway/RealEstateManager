package yofaraway.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import yofaraway.openclassrooms.realestatemanager.database.converter.Converters
import yofaraway.openclassrooms.realestatemanager.database.dao.EstateDao
import yofaraway.openclassrooms.realestatemanager.model.Estate

@Database(entities = [Estate::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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