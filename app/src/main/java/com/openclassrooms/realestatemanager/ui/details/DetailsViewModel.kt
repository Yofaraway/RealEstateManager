package com.openclassrooms.realestatemanager.ui.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.model.Estate

class DetailsViewModel : ViewModel() {

var estate = MutableLiveData<Estate>()

    fun init(detailsEstate: Estate) {
        this.estate.value = detailsEstate

    }

}