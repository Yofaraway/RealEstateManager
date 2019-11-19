package com.openclassrooms.realestatemanager.ui.addestate

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AddEstateViewModel : ViewModel() {

    // init Inputs (2 ways data-binding)
    val type = MutableLiveData<String>()
    val surface = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val rooms = MutableLiveData<String>()
    val bathrooms = MutableLiveData<String>()
    val bedrooms = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val dateAvailable = MutableLiveData<String>()
    val dateSold = MutableLiveData<String>()
    val agent = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val hasBeenSold = MutableLiveData<Boolean>()
    val dateAvailableDatePicker = MutableLiveData<Boolean>()
    val dateSoldDatePicker = MutableLiveData<Boolean>()

    // validation
    val showError = MutableLiveData<Boolean>(false)


    // Spinner listener
    val onStatusSelected = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (position) {
                0 -> hasBeenSold.value = false
                1 -> hasBeenSold.value = true
            }
        }
    }

    fun onDateAvailableClick() {
        dateAvailableDatePicker.value = true
    }

    fun onDateSoldClick(){
        dateSoldDatePicker.value = true
    }


    // Button listener
    fun onAddBtnClick() {
        showError.value = true

    }



}
