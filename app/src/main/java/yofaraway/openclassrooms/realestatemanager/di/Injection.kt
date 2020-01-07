package yofaraway.openclassrooms.realestatemanager.di

import android.content.Context
import yofaraway.openclassrooms.realestatemanager.database.RealEstateDatabase
import yofaraway.openclassrooms.realestatemanager.repository.EstateDataRepository
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