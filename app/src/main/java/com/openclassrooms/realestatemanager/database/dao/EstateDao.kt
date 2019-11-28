package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.model.Estate

@Dao
interface EstateDao {
    @Query("SELECT * FROM estates")
    fun getEstates(): LiveData<List<Estate>>

    @Query("SELECT * FROM estates WHERE id = :id")
    fun getEstateWithId(id: Long): LiveData<Estate>

    @Insert
    fun insertEstate(estate: Estate): Long

    @Update
    fun updateEstate(estate: Estate): Int

}