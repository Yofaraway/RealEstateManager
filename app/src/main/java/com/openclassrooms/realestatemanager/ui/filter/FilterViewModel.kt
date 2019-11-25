package com.openclassrooms.realestatemanager.ui.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class FilterViewModel : ViewModel() {

    // CHECKBOXES
    var isPriceChecked = MutableLiveData<Boolean>()
    var isSurfaceChecked = MutableLiveData<Boolean>()
    var isPhotoChecked = MutableLiveData<Boolean>()
    var isAvailableChecked = MutableLiveData<Boolean>()
    var isSoldChecked = MutableLiveData<Boolean>()
    var isNearChecked = MutableLiveData<Boolean>()
    // PRICE VALUES
    var priceMin = MutableLiveData<Int>()
    var priceMax = MutableLiveData<Int>()
    // SURFACE VALUES
    var surfaceMin = MutableLiveData<Int>()
    var surfaceMax = MutableLiveData<Int>()
    // PHOTO VALUES
    var photoMin = MutableLiveData<Int>()
    var photoMax = MutableLiveData<Int>()
    // DATE VALUES
    var fromAvailable = MutableLiveData<Date>()
    var afterSold = MutableLiveData<Date>()
    var beforeSold = MutableLiveData<Date>()
    var isSoldAfterChecked = MutableLiveData<Boolean>()
    var isSoldBeforeChecked = MutableLiveData<Boolean>()





    fun init() {
        // CHECKBOXES
        for (isSelected in getCheckboxes()) {
            isSelected.value = false
        }
        isSoldAfterChecked.value = false
        isSoldBeforeChecked.value = false
    }

    private fun getCheckboxes(): MutableSet<MutableLiveData<Boolean>> {
        return mutableSetOf(
            isPriceChecked,
            isSurfaceChecked,
            isPhotoChecked,
            isAvailableChecked,
            isSoldChecked,
            isNearChecked
        )

    }


}
