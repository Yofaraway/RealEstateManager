package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.model.Estate

class EstateDataRepository constructor(private val database: RealEstateDatabase) {

    fun getEstates(): LiveData<List<Estate>> {
        return this.database.estateDao().getEstates()
    }

    fun getEstateWithId(id: Long): LiveData<Estate>{
        return this.database.estateDao().getEstateWithId(id)
    }

    fun createTask(estate: Estate){
        this.database.estateDao().insertEstate(estate)
    }

    fun updateEstate(estate: Estate) {
        this.database.estateDao().updateEstate(estate)
    }


}