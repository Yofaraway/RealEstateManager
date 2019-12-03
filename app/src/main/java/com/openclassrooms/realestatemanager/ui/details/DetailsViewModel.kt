package com.openclassrooms.realestatemanager.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.utils.getAddress
import com.openclassrooms.realestatemanager.utils.getCity
import com.openclassrooms.realestatemanager.utils.getZipCode
import java.util.*

class DetailsViewModel : ViewModel() {

    var estate = MutableLiveData<Estate>()
    var city = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var zipCode = MutableLiveData<String>()
    var price = MutableLiveData<Int>()
    var type = MutableLiveData<String>()
    var agent = MutableLiveData<String>()
    var dateAvailable = MutableLiveData<Date>()
    var dateSold = MutableLiveData<Date>()
    var hasBeenSold = MutableLiveData<Boolean>()


    fun init(detailsEstate: Estate) {
        this.estate.value = detailsEstate
        formatAddress()
        price.value = detailsEstate.priceDollars
        type.value = detailsEstate.type
        agent.value = detailsEstate.agent
        dateAvailable.value = detailsEstate.dateAvailable
        hasBeenSold.value = detailsEstate.hasBeenSold
        if (detailsEstate.hasBeenSold) dateSold.value = detailsEstate.dateSold
    }


    private fun formatAddress() {
        val fullAddress = estate.value!!.address
        address.value = getAddress(fullAddress)
        zipCode.value = getZipCode(fullAddress)
        city.value = getCity(fullAddress)
    }

}