package com.openclassrooms.realestatemanager.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.utils.Utils.getAddress
import com.openclassrooms.realestatemanager.utils.Utils.getCity
import com.openclassrooms.realestatemanager.utils.Utils.getZipCode
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

    // Currency
    var currency: String = "Dollar"


    fun init(detailsEstate: Estate, currentCurrency: String) {
        this.estate.value = detailsEstate
        currency = currentCurrency

        formatAddress()

        if (currency == "Euro") price.value = convertDollarToEuro(detailsEstate.priceDollars)
        else price.value = detailsEstate.priceDollars
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