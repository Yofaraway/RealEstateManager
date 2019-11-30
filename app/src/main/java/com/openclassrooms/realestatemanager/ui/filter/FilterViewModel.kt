package com.openclassrooms.realestatemanager.ui.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import java.util.*

class FilterViewModel : ViewModel() {

    // CHECKBOXES
    var isPriceChecked = MutableLiveData<Boolean>()
    var isSurfaceChecked = MutableLiveData<Boolean>()
    var isPhotoChecked = MutableLiveData<Boolean>()
    var isAvailableChecked = MutableLiveData<Boolean>()
    var isSoldChecked = MutableLiveData<Boolean>()
    var isSoldAfterChecked = MutableLiveData<Boolean>()
    var isSoldBeforeChecked = MutableLiveData<Boolean>()
    var isNearChecked = MutableLiveData<Boolean>()
    // PRICE VALUES
    var priceMin = MutableLiveData<Int>()
    var priceMax = MutableLiveData<Int>()
    // SURFACE VALUES
    var surfaceMin = MutableLiveData<Int>()
    var surfaceMax = MutableLiveData<Int>()
    // PHOTO VALUES
    var photoMin = MutableLiveData<Int>()
    var photoMax = MutableLiveData<Int>()
    // DATE VALUES
    var fromAvailable = MutableLiveData<Date>()
    var afterSold = MutableLiveData<Date>()
    var beforeSold = MutableLiveData<Date>()

    // NEAR VALUES
    var nearPlaces = MutableLiveData<MutableList<String>>()


    fun init() {
        // CHECKBOXES
        for (isSelected in getCheckboxes()) {
            isSelected.value = false
        }
        nearPlaces.value = mutableListOf()
    }

    fun atLeastOneChecked(): Boolean {
        return getCheckboxes().any { it.value == true }
    }

    private fun getCheckboxes(): MutableSet<MutableLiveData<Boolean>> {
        return mutableSetOf(
            isPriceChecked,
            isSurfaceChecked,
            isPhotoChecked,
            isAvailableChecked,
            isSoldChecked,
            isSoldAfterChecked,
            isSoldBeforeChecked,
            isNearChecked
        )
    }

    // FILTERS

    fun getListFiltered(list: List<Estate>): List<Estate> {
        var listToFilter = list
        if (isPriceChecked.value!!) listToFilter =
            getListFilteredByPrice(listToFilter, priceMin.value!!, priceMax.value!!)

        if (isSurfaceChecked.value!!) listToFilter =
            getListFilteredBySurface(listToFilter, surfaceMin.value!!, surfaceMax.value!!)

        if (isPhotoChecked.value!!) listToFilter =
            getListFilteredByPhotos(listToFilter, photoMin.value!!, photoMax.value!!)

        if (isAvailableChecked.value!!) listToFilter =
            getListFilteredByDateAvailability(listToFilter, fromAvailable.value!!)

        if (isSoldAfterChecked.value!!) listToFilter =
            getListFilteredByDateSoldAfter(listToFilter, afterSold.value!!)

        if (isSoldBeforeChecked.value!!) listToFilter =
            getListFilteredByDateSoldBefore(listToFilter, beforeSold.value!!)

        if (isNearChecked.value!!) listToFilter =
            getListFilteredByNearPlaces(listToFilter, nearPlaces.value!!)

        return listToFilter
    }


    // BY PRICE
    private fun getListFilteredByPrice(
        list: List<Estate>,
        priceMin: Int,
        priceMax: Int
    ): List<Estate> {
        val listFiltered: MutableList<Estate> = mutableListOf()

        for (estate in list) {
            if (estate.priceDollars in priceMin..priceMax)
                listFiltered.add(estate)
        }
        println(listFiltered.size)
        return listFiltered
    }

    // BY SURFACE
    private fun getListFilteredBySurface(
        list: List<Estate>,
        surfaceMin: Int,
        surfaceMax: Int
    ): List<Estate> {
        val listFiltered: MutableList<Estate> = mutableListOf()

        for (estate in list) {
            if (estate.surface in surfaceMin..surfaceMax)
                listFiltered.add(estate)
        }
        return listFiltered
    }

    // BY NUMBER OF PHOTOS
    private fun getListFilteredByPhotos(
        list: List<Estate>,
        photoMin: Int,
        photoMax: Int
    ): List<Estate> {
        val listFiltered: MutableList<Estate> = mutableListOf()

        for (estate in list) {
            if (estate.pathPhotos.size in photoMin..photoMax)
                listFiltered.add(estate)
        }
        return listFiltered
    }

    // BY DATE OF AVAILABILITY
    private fun getListFilteredByDateAvailability(
        list: List<Estate>,
        dateFrom: Date
    ): List<Estate> {
        val listFiltered: MutableList<Estate> = mutableListOf()

        for (estate in list) {
            if (estate.dateAvailableSince.after(dateFrom) || estate.dateAvailableSince.compareTo(dateFrom) == 0)
                listFiltered.add(estate)
        }
        return listFiltered
    }

    // BY DATE OF SALE
    private fun getListFilteredByDateSoldAfter(list: List<Estate>, dateAfter: Date): List<Estate> {
        val listFiltered: MutableList<Estate> = mutableListOf()

        for (estate in list) {
            if (estate.dateSold != null) {
                val dateSold = estate.dateSold
                if (dateSold!!.after(dateAfter) || dateSold.compareTo(dateAfter) == 0)
                    listFiltered.add(estate)
            }
        }
        return listFiltered
    }

    private fun getListFilteredByDateSoldBefore(
        list: List<Estate>,
        dateBefore: Date
    ): List<Estate> {
        val listFiltered: MutableList<Estate> = mutableListOf()

        for (estate in list) {
            if (estate.dateSold != null) {
                val dateSold = estate.dateSold
                if (dateSold!!.before(dateBefore) || dateSold.compareTo(dateBefore) == 0)
                    listFiltered.add(estate)
            }

        }
        return listFiltered
    }

    // BY NEARBY PLACES
    private fun getListFilteredByNearPlaces(
        list: List<Estate>,
        nearPlaces: List<String>
    ): List<Estate> {
        val listFiltered: MutableList<Estate> = mutableListOf()

        for (estate in list) {
            if (estate.nearTo.containsAll(nearPlaces))
                listFiltered.add(estate)
        }
        return listFiltered
    }


}
