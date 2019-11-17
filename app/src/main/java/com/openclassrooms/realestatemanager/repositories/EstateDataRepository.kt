package com.openclassrooms.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.models.Estate

class EstateDataRepository(private val database: RealEstateDatabase) {

    fun getEstates(): LiveData<List<Estate>> {
        return this.database.estateDao().getEstates()
    }

    fun createTask(estate: Estate) {
        this.database.estateDao().insertEstate(estate)
    }

    fun updateEstate(estate: Estate) {
        this.database.estateDao().updateEstate(estate)
    }


}