package com.openclassrooms.realestatemanager.ui.addestate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.utils.stringToDate


class AddEstateViewModel : ViewModel() {
    // init Inputs (2 ways data-binding)
    val type = MutableLiveData<String>()
    val surface = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val rooms = MutableLiveData<String>()
    val bathrooms = MutableLiveData<String>()
    val bedrooms = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val status = MutableLiveData<String>()
    val dateAvailable = MutableLiveData<String>()
    val dateSold = MutableLiveData<String>()
    val agent = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val hasBeenSold = MutableLiveData<Boolean>(false)
    var pathToPhotos = MutableLiveData<MutableList<String?>>()
    var titlesPhotos = MutableLiveData<MutableList<String?>>()
    val atLeastOnePhoto = MutableLiveData<Boolean>(false)
    // validation
    val showError = MutableLiveData<Boolean>(false)
    val dateAvailableDatePicker = MutableLiveData<Boolean>()
    val dateSoldDatePicker = MutableLiveData<Boolean>()
    lateinit var newEstate: Estate
    val addNewEstate = MutableLiveData<Boolean>(false)

    private var statusDefaultForReset: String = ""

    fun init(statusDefault: String) {
        if (pathToPhotos.value == null) {
            pathToPhotos.value = mutableListOf()
            titlesPhotos.value = mutableListOf()
            statusDefaultForReset = statusDefault
            status.value = statusDefault
        }
    }

    fun reset() {
        type.value = ""
        surface.value = ""
        price.value = ""
        rooms.value = ""
        bathrooms.value = ""
        bedrooms.value = ""
        surface.value = ""
        address.value = ""
        status.value = statusDefaultForReset
        dateAvailable.value = ""
        dateSold.value = ""
        hasBeenSold.value = false
        agent.value = ""
        description.value = ""
        pathToPhotos.value = mutableListOf()
        titlesPhotos.value = mutableListOf()
        atLeastOnePhoto.value = false
        showError.value = false

    }


    // Date listeners (observers in the fragment)
    fun onDateAvailableClick() {
        dateAvailableDatePicker.value = true
    }

    fun onDateSoldClick() {
        dateSoldDatePicker.value = true
    }

    // on button click (listener with data binding)
    fun onAddBtnClick() {
        if (!checkEmptyEditTexts(getInputs()) && atLeastOnePhoto.value!!) createNewEstate()
        else showError.value = true
    }

    private fun getInputs(): MutableSet<String?> {
        val inputs = mutableSetOf(
            type.value,
            price.value,
            surface.value,
            rooms.value,
            bathrooms.value,
            bedrooms.value,
            description.value,
            address.value,
            dateAvailable.value,
            agent.value
        )
        if (hasBeenSold.value!!) inputs.add(dateSold.value)
        return inputs
    }

    private fun checkEmptyEditTexts(inputs: MutableSet<String?>): Boolean {
        return (inputs.any { it == null || it == "" })
    }

    private fun createNewEstate() {
        val pathList = getListWithoutNull(pathToPhotos.value!!)
        val titlesList = getListWithoutNull(titlesPhotos.value!!)
        val dateA = stringToDate(dateAvailable.value!!)
        val dateS =
            when (hasBeenSold.value!!) {
                true -> stringToDate(dateSold.value!!)
                false -> null
            }

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
            address.value!!,
            "",
            hasBeenSold.value!!,
            dateA,
            dateS,
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
