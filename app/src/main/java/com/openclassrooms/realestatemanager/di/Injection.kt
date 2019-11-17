package com.openclassrooms.realestatemanager.di

import android.content.Context
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {


    fun provideEstateDataSource(context: Context): EstateDataRepository {
        val database = RealEstateDatabase.getInstance(context)
        return EstateDataRepository(database)
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceEstate = provideEstateDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceEstate, executor)
    }

}