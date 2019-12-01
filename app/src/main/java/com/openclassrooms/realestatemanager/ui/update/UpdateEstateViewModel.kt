package com.openclassrooms.realestatemanager.ui.update

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.utils.getAddress
import com.openclassrooms.realestatemanager.utils.getCity
import com.openclassrooms.realestatemanager.utils.getZipCode
import java.util.*
import kotlin.collections.ArrayList

class UpdateEstateViewModel : ViewModel() {

    var estate:Estate? = null

    // INPUTS RECEIVERS (2 ways data-binding)

    // EDIT TEXT
    val type = MutableLiveData<String>()
    val surface = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val rooms = MutableLiveData<String>()
    val bathrooms = MutableLiveData<String>()
    val bedrooms = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val addressCity = MutableLiveData<String>()
    val addressZipCode = MutableLiveData<String>()
    val agent = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    // STATUS
    val hasBeenSold = MutableLiveData<Boolean>(false)
    val status = MutableLiveData<String>()
    // DATE PICKERS
    val dateAvailable = MutableLiveData<Date>()
    val dateSold = MutableLiveData<Date>()
    // NEAR PLACES (CHIPS & CHECKBOXES)
    var nearPlaces = MutableLiveData<List<String?>>(listOf())
    lateinit var placesChoicesCheckedList: BooleanArray
    // PHOTOS
    var photoPathList = MutableLiveData<MutableList<String>>()
    var photoTitleList = MutableLiveData<MutableList<String>>()
    val atLeastOnePhoto = MutableLiveData<Boolean>(false)


    // CHECK INPUTS
    val showError = MutableLiveData<Boolean>(false)
    lateinit var newEstate: Estate
    val updateEstate = MutableLiveData<Boolean>(false)


    // photo variables needed
    var newPhotoPath: String? = null


    fun init(estate: Estate, strAvailable: String, strSold: String, listNearPlaces: Array<String>) {
        this.estate = estate
        type.value = estate.type
        surface.value = estate.surface.toString()
        price.value = estate.priceDollars.toString()
        rooms.value = estate.rooms.toString()
        bathrooms.value = estate.bathrooms.toString()
        bedrooms.value = estate.bedrooms.toString()
        address.value = getAddress(estate.address)
        addressCity.value = getCity(estate.address)
        addressZipCode.value = getZipCode(estate.address)
        agent.value = estate.agent
        description.value = estate.description
        dateAvailable.value = estate.dateAvailableSince
        nearPlaces.value = estate.nearTo
        placesChoicesCheckedList = BooleanArray(listNearPlaces.size)
        photoTitleList.value?.addAll(estate.titlesPhotos)
        photoPathList.value?.addAll(estate.pathPhotos)


        // for each places in the list, set true for this place index in the list of booleans
        for (strPlace in estate.nearTo) {
            placesChoicesCheckedList[listNearPlaces.indexOf(strPlace)] = true
        }

        // Date sold
        if (estate.dateSold != null) {
            hasBeenSold.value = true
            dateSold.value = estate.dateSold
            status.value = strSold
        } else {
            hasBeenSold.value = false
            status.value = strAvailable
        }


    }




    // on button click (listener with data binding)
    fun onAddBtnClick() {
        if (!checkEmptyEditTexts(getStringInputs())
            && !checkEmptyDates(getDateInputs())
            && atLeastOnePhoto.value!!
        ) //createNewEstate()
        else showError.value = true
    }


    private fun getStringInputs(): MutableSet<String?> {
        return mutableSetOf(
            type.value,
            price.value,
            surface.value,
            rooms.value,
            bathrooms.value,
            bedrooms.value,
            description.value,
            address.value,
            addressCity.value,
            addressZipCode.value,
            agent.value
        )

    }


    private fun getDateInputs(): MutableSet<Date?> {
        val inputs = mutableSetOf(
            dateAvailable.value
        )
        if (hasBeenSold.value!!) inputs.add(dateSold.value)
        return inputs
    }


    private fun checkEmptyEditTexts(inputs: MutableSet<String?>): Boolean {
        return (inputs.any { it == null || it == "" })
    }


    private fun checkEmptyDates(inputs: MutableSet<Date?>): Boolean {
        return (inputs.any { it == null })
    }



    private fun getListWithoutNull(mutableList: MutableList<String?>): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        for (entry in mutableList) {
            if (entry != null) list.add(entry)
        }
        return list
    }


}

