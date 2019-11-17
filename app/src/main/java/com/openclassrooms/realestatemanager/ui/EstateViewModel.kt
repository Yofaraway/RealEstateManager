package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.Estate
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import java.util.concurrent.Executor

class EstateViewModel(
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


    fun createEstate(estate: Estate) {
        executor.execute { estateDataSource.createTask(estate) }

    }

}
