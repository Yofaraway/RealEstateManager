package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateDataRepository
import java.util.concurrent.Executor

class EstatesViewModel (
    private val estateDataSource: EstateDataRepository,
    private val executor: Executor) : ViewModel() {
    internal var estates: LiveData<List<Estate>>? = null

    fun init() {
        if (this.estates != null) {
            return
        }
        estates = estateDataSource.getEstates()
    }


    fun getEstates(): LiveData<List<Estate>> {
        return estateDataSource.getEstates()
    }

    fun getEstateWithId(id: Long): LiveData<List<Estate>> {
        return estateDataSource.getEstateWithId(id)
    }


    fun createEstate(estate: Estate) {
        executor.execute { estateDataSource.createTask(estate) }

    }

    override fun onCleared() {
        super.onCleared()
    }

}
