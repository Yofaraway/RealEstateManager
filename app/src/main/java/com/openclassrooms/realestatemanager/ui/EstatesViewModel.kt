package com.openclassrooms.realestatemanager.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.repository.EstateDataRepository
import java.util.concurrent.Executor

class EstatesViewModel(
    private val estateDataSource: EstateDataRepository,
    private val executor: Executor
) : ViewModel() {
    private var estates: LiveData<List<Estate>>? = null
    var estatesFiltered = MutableLiveData<List<Estate>?>()

    fun init() {
        if (this.estates != null) {
            return
        }
        estates = estateDataSource.getEstates()
        estatesFiltered.value = estates?.value
    }


    fun getEstates(): LiveData<List<Estate>> {
        return estateDataSource.getEstates()
    }

    fun getEstateWithId(id: Long): LiveData<Estate> {
        return estateDataSource.getEstateWithId(id)
    }


    fun createEstate(estate: Estate) {
        executor.execute { estateDataSource.createTask(estate) }
    }


}
