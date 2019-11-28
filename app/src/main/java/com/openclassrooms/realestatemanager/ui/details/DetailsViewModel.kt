package com.openclassrooms.realestatemanager.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate

class DetailsViewModel : ViewModel() {

    var estate = MutableLiveData<Estate>()
    var city = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var zipCode = MutableLiveData<String>()
    var price = MutableLiveData<Int>()


    fun init(detailsEstate: Estate) {
        this.estate.value = detailsEstate
        formatAddress()
        price.value = detailsEstate.priceDollars

    }

    private fun formatAddress() {
        val full = estate.value!!.address
        address.value = full.substring(0, full.indexOf("-")).capitalize()
        zipCode.value = full.substring(full.lastIndexOf("-") + 1)
        city.value = full.substring(full.indexOf("-") + 1, full.lastIndexOf("-")).capitalize()

    }

}