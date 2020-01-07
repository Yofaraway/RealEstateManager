package yofaraway.openclassrooms.realestatemanager.ui.addestate

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import yofaraway.openclassrooms.realestatemanager.model.Estate
import yofaraway.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar
import java.util.*


class AddEstateViewModel : ViewModel() {


    // INPUTS (2 ways data-binding)

    // EDIT TEXT
    val type = MutableLiveData<String>()
    val surface = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val rooms = MutableLiveData<String>()
    val bathrooms = MutableLiveData<String>()
    val bedrooms = MutableLiveData<String>()
    val address = MutableLiveData<String>()
    val addressCity = MutableLiveData<String>()
    val addressZipCode = MutableLiveData<String>()
    val agent = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    // STATUS
    val hasBeenSold = MutableLiveData<Boolean>(false)
    val status = MutableLiveData<String>()
    private var statusDefaultForReset: String = ""
    // DATE PICKERS
    val dateAvailable = MutableLiveData<Date>()
    val dateSold = MutableLiveData<Date>()
    // NEAR PLACES (CHIPS & CHECKBOXES)
    var nearPlaces = MutableLiveData<List<String?>>(listOf())
    lateinit var placesChoicesCheckedList: BooleanArray
    // PHOTOS
    var photoPathList = MutableLiveData<MutableList<String>>()
    var photoTitleList = MutableLiveData<MutableList<String>>()
    val atLeastOnePhoto = MutableLiveData<Boolean>(false)
    var newPhotoPath:String? = null

    // CHECK INPUTS
    val showError = MutableLiveData<Boolean>(false)
    lateinit var newEstate: Estate
    val addNewEstate = MutableLiveData<Boolean>(false)

    // CURRENCY
    var currency:String? = "Dollar"



    fun init(statusDefault: String, placesChoicesListSize: Int, currentCurrency: String) {
        // Photo
        photoPathList.value = mutableListOf()
        photoTitleList.value = mutableListOf()
        // Status
        statusDefaultForReset = statusDefault
        status.value = statusDefault
        // Near places
        placesChoicesCheckedList = BooleanArray(placesChoicesListSize)

        currency = currentCurrency
    }




    // on button click (listener with data binding)
    fun onConfirmBtnClick() {
        if (!checkEmptyEditTexts(getStringInputs())
            && !checkEmptyDates(getDateInputs())
            && atLeastOnePhoto.value!!
        ) createNewEstate()
        else showError.value = true
    }


    private fun getStringInputs(): MutableSet<String?> {
        return mutableSetOf(
            type.value,
            price.value,
            surface.value,
            rooms.value,
            bathrooms.value,
            bedrooms.value,
            description.value,
            address.value,
            addressCity.value,
            addressZipCode.value,
            agent.value
        )
    }


    private fun getDateInputs(): MutableSet<Date?> {
        val inputs = mutableSetOf(
            dateAvailable.value
        )
        if (hasBeenSold.value!!) inputs.add(dateSold.value)
        return inputs
    }


    private fun checkEmptyEditTexts(inputs: MutableSet<String?>): Boolean {
        return (inputs.any { it == null || it == "" })
    }


    private fun checkEmptyDates(inputs: MutableSet<Date?>): Boolean {
        return (inputs.any { it == null })
    }


    private fun createNewEstate() {
        val addressComplete = "${address.value}-${addressCity.value}-${addressZipCode.value}"
        if (!hasBeenSold.value!!) dateSold.value = null

        if (currency == "Euro") price.value = convertEuroToDollar(price.value!!.toInt()).toString()

        newEstate = Estate(
            null,
            type.value!!,
            price.value!!.toInt(),
            surface.value!!.toInt(),
            rooms.value!!.toInt(),
            bathrooms.value!!.toInt(),
            bedrooms.value!!.toInt(),
            description.value!!,
            photoPathList.value!!.toList(),
            photoTitleList.value!!.toList(),
            addressComplete,
            mutableListOf(),
            nearPlaces.value!!,
            hasBeenSold.value!!,
            dateAvailable.value!!,
            dateSold.value,
            agent.value!!
        )
        addNewEstate.value = true
    }
}
