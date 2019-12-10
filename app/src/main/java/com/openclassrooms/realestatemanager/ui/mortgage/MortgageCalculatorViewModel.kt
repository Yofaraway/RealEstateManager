package com.openclassrooms.realestatemanager.ui.mortgage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MortgageCalculatorViewModel : ViewModel() {

    var amount = MutableLiveData<String>()
    var interest = MutableLiveData<String>()
    var result = MutableLiveData<String>()
    var years = MutableLiveData<String>()


}
