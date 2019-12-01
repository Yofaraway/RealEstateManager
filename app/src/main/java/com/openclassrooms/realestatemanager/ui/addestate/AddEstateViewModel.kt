package com.openclassrooms.realestatemanager.ui.addestate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import java.util.*
import kotlin.collections.ArrayList


class AddEstateViewModel : ViewModel() {


    // INPUTS (2 ways data-binding)

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
    private var statusDefaultForReset: String = ""
    // DATE PICKERS
    val dateAvailable = MutableLiveData<Date>()
    val dateSold = MutableLiveData<Date>()
    // NEAR PLACES (CHIPS & CHECKBOXES)
    var nearPlaces = MutableLiveData<List<String?>>(listOf())
    lateinit var placesChoicesCheckedList: BooleanArray
    // PHOTOS
    var pathToPhotos = MutableLiveData<MutableList<String?>>()
    var titlesPhotos = MutableLiveData<MutableList<String?>>()
    val atLeastOnePhoto = MutableLiveData<Boolean>(false)


    // CHECK INPUTS
    val showError = MutableLiveData<Boolean>(false)
    lateinit var newEstate: Estate
    val addNewEstate = MutableLiveData<Boolean>(false)

    // photo variables needed
    var indexPhotos:Int = 0
    var newPhotoPath:String? = null


    fun init(statusDefault: String, placesChoicesListSize: Int) {
        // Photo
        pathToPhotos.value = mutableListOf()
        titlesPhotos.value = mutableListOf()
        // Status
        statusDefaultForReset = statusDefault
        status.value = statusDefault
        // Near places
        placesChoicesCheckedList = BooleanArray(placesChoicesListSize)
    }




    // on button click (listener with data binding)
    fun onAddBtnClick() {
        if (!checkEmptyEditTexts(getStringInputs())
            && !checkEmptyDates(getDateInputs())
            && atLeastOnePhoto.value!!
        ) createNewEstate()
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


    private fun createNewEstate() {
        val pathList = getListWithoutNull(pathToPhotos.value!!)
        val titlesList = getListWithoutNull(titlesPhotos.value!!)
        val addressComplete = "${address.value} -${addressCity.value} -${addressZipCode.value}"

        newEstate = Estate(
            null,
            type.value!!,
            price.value!!.toInt(),
            surface.value!!.toInt(),
            rooms.value!!.toInt(),
            bathrooms.value!!.toInt(),
            bedrooms.value!!.toInt(),
            description.value!!,
            pathList,
            titlesList,
            addressComplete,
            mutableListOf(),
            nearPlaces.value!!,
            hasBeenSold.value!!,
            dateAvailable.value!!,
            dateSold.value,
            agent.value!!
        )
        addNewEstate.value = true
    }


    private fun getListWithoutNull(mutableList: MutableList<String?>): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        for (entry in mutableList) {
            if (entry != null) list.add(entry)
        }
        return list
    }


}
