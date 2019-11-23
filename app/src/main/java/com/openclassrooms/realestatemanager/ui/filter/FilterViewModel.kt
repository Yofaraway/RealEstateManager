package com.openclassrooms.realestatemanager.ui.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {
    var isAvailableChecked = MutableLiveData<Boolean>()
    var isSoldChecked = MutableLiveData<Boolean>()
}
