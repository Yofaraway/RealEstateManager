package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.models.Estate

interface EstateDao {
    @Query("SELECT * FROM estates")
    fun getEstates(): LiveData<List<Estate>>

    @Query("SELECT * FROM estates WHERE agent = :agent")
    fun findEstatesByAgentName(agent: String): LiveData<Estate>

    @Insert
    fun insertEstate(estate: Estate): Long

    @Update
    fun updateEstate(estate: Estate): Int
}