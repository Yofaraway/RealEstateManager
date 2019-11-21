package com.openclassrooms.realestatemanager.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.repository.EstateDataRepository
import com.openclassrooms.realestatemanager.ui.EstatesViewModel
import java.util.concurrent.Executor

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val estateDataSource: EstateDataRepository, private val executor: Executor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstatesViewModel::class.java)) {
            return EstatesViewModel(
                estateDataSource,
                executor
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}