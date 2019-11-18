package com.openclassrooms.realestatemanager.ui.addestate

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


    fun test():Boolean = true



fun onAddBtnClick(){
    if (type.value.toString() == "" || type.value == null) println("empty")
}



    fun init() {
        //toDO
    }

}